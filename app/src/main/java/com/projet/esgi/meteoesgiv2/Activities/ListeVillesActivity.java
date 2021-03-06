package com.projet.esgi.meteoesgiv2.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.projet.esgi.meteoesgiv2.MeteoAPI.FiveDaysWeatherTask;
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
                    FiveDaysWeatherTask searchTask = new FiveDaysWeatherTask();
                    searchTask.execute(nomVille, getBaseContext().getString(R.string.langue_API));
                    try{
                        MeteoData[] m = searchTask.get();
                        if(null == m){
                            Toast.makeText(getApplicationContext(),(R.string.erreur_ville_inconnue),Toast.LENGTH_SHORT).show();
                        }else{
                            Ville v = new Ville();
                            v.setNom(nomVille);
                            v.setMeteoData(m);
                            ((MeteoApplication)getApplication()).addVille(v);
                            lesVilles.add(v);
                        }
                    }catch (Exception e){
                        Log.e("VilleActivity", "Erreur lors de l'exécution de la tâche asynchrone", e);
                    }
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
        Intent intent = new Intent(this,VilleSliderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ville", (java.io.Serializable) listeDesVilles.getItemAtPosition(position));
        intent.putExtras(bundle);
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
        listeDesVilles.setItemsCanFocus(true);
        registerForContextMenu(listeDesVilles);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Ouvrir");
        menu.add(0, v.getId(), 1, "Supprimer");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Supprimer"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            lesVillesFavoris.remove(info.position);
            lesVilles.remove(info.position);
            ((MeteoApplication)getApplication()).removeVille((Ville)listeDesVilles.getItemAtPosition(info.position));
            ((MeteoApplication)getApplication()).removeFavoris((Ville)listeDesVilles.getItemAtPosition(info.position));
            adapterVille.notifyDataSetChanged();
            listeDesVilles.deferNotifyDataSetChanged();
        }
        if(item.getTitle()=="Ouvrir"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            onVilleChoisieClick(info.position);
        }

        return true;
    }

    private void initSearchView() {
        rechercheVilleListe = (SearchView) findViewById(R.id.rechercherVille);
        rechercheVilleListe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                searchVille(query);
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

    @Override
    public void onBackPressed() {
        return;
    }
}
