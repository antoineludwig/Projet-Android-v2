package com.projet.esgi.meteoesgiv2.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.projet.esgi.meteoesgiv2.MeteoAPI.CurrentWeatherTask;
import com.projet.esgi.meteoesgiv2.MeteoApplication;
import com.projet.esgi.meteoesgiv2.R;
import com.projet.esgi.meteoesgiv2.adapter.AdapterListeVille;
import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

import java.util.ArrayList;

public class ListeVillesActivity extends Activity {

    private ListView listeDesVilles;
    private Switch switchFavoris;
    private SearchView rechercheVilleListe;
    private AdapterListeVille adapterVille;

    private static ArrayList<Ville> lesVilles = new ArrayList<Ville>();
    private static ArrayList<Ville> lesVillesFavoris = new ArrayList<Ville>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_villes);


        lesVilles = ((MeteoApplication)getApplication()).getLesVilles();
        initListVille(lesVilles,false);

        lesVillesFavoris= ((MeteoApplication)getApplication()).getLesVillesFavoris();
        initFavoris();

        initSearchView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_villes, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //menu a propos
        if (id == R.id.action_apropos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.aPropos))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        //menu ajout d'une ville
        if(id == R.id.action_ajout_ville){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Ajouter une ville");
            alert.setMessage("Veuillez saisir le nom de la ville");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String nomVille = input.getText().toString();

                    //ajout de la ville si elle existe
                    CurrentWeatherTask searchTask = new CurrentWeatherTask();
                    searchTask.execute(nomVille, getBaseContext().getString(R.string.langue_API));
                    try{
                        MeteoData m = searchTask.get();
                        if(null == m){

                        }else{
                            Ville v = new Ville();
                            v.setNom(nomVille);
                            v.setMeteoData(m);
                            lesVillesFavoris.add(v);
                        }
                    }catch (Exception e){
                        Log.e("VilleActivity", "Erreur lors de l'exécution de la tâche asynchrone", e);
                    }


                    Toast.makeText(getApplicationContext(),nomVille,Toast.LENGTH_SHORT).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onVilleChoisieClick(int position){
        /*Intent intent = new Intent(this,VilleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ville", (java.io.Serializable) listeDesVilles.getItemAtPosition(position));
        intent.putExtras(bundle);
        startActivity(intent);*/
        Intent intent = new Intent(this,ScreenSlidePagerActivity.class);
        startActivity(intent);
    }

    public void initFavoris(){
        //liste des favoris
        switchFavoris = (Switch) findViewById(R.id.favoris);
        switchFavoris.setChecked(false);
        switchFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeListToFavoris(view);
            }
        });
        switchFavoris.setChecked(true);
        switchFavoris.callOnClick();
    }

    public void changeListToFavoris(View view){

        if(switchFavoris.isChecked())
        {
            initListVille(lesVillesFavoris,true);
        }
        else
        {
            initListVille(lesVilles,false);
        }
    }

    public void initListVille (ArrayList<Ville> listeVilles,boolean isFavoris){
        adapterVille = new AdapterListeVille(this,listeVilles,this,isFavoris);


        //liste des villes
        listeDesVilles = (ListView) findViewById(R.id.listeVille);
        listeDesVilles.setAdapter(adapterVille);
        /*listeDesVilles.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onVilleChoisieClick(i);
            }
        });*/
    }



    private void initSearchView() {
        rechercheVilleListe = (SearchView) findViewById(R.id.rechercherVille);
        rechercheVilleListe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                searchVille(query);
                // Reset SearchView
                //rechercheVilleListe.clearFocus();
                //rechercheVilleListe.setQuery("", false);
                //rechercheVilleListe.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        rechercheVilleListe.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    initListVille(lesVilles,false);
            }
        });
    }

    private void searchVille(String query){
        ArrayList<Ville> lesVillesRecherchees = new ArrayList<Ville>();
        if(query.length()==0){
            initListVille(lesVilles,false);
        }else{
            for(Ville v : lesVilles){
                if (v.getNom().toLowerCase().contains((CharSequence)query.toLowerCase())){
                    lesVillesRecherchees.add(v);
                }
            }
            initListVille(lesVillesRecherchees,false);
        }

    }

    public void deleteVille(int index){
        lesVillesFavoris.get(index).setFavoris(false);
        lesVillesFavoris.remove(index);
        adapterVille.notifyDataSetChanged();
        listeDesVilles.deferNotifyDataSetChanged();
    }
}
