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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projet.esgi.meteoesgiv2.MeteoApplication;
import com.projet.esgi.meteoesgiv2.R;
import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VilleActivity extends Activity {

    private static final String FICHIER_CACHE = "cache_donnees_meteo";
    private Button boutonRetour;
    private CheckBox checkFavoris;
    private Ville laVille = new Ville();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville);

        Intent i = getIntent();
        laVille = (Ville)i.getSerializableExtra("ville");

        initElements();
       // launchSearchTask();
        initAffichageVille();
    }

    private void initElements(){
        boutonRetour = (Button) findViewById(R.id.retour);
        checkFavoris = (CheckBox) findViewById(R.id.isFavori);
        checkFavoris.setChecked(laVille.isFavoris());
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VilleActivity.this,ListeVillesActivity.class);
                startActivity(intent);
            }
        });
        checkFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFavoris.isChecked()){
                    ((MeteoApplication)getApplication()).addFavoris(laVille);
                }else{
                    ((MeteoApplication)getApplication()).removeFavoris(laVille);
                }


            }
        });
    }

    private void initAffichageVille() {
        TextView nomDeLaVille = (TextView)findViewById(R.id.nomVillea);
        nomDeLaVille.setText(laVille.getNom());

        MeteoData meteoData = laVille.getMeteoData();
        if(meteoData != null) {
            TextView temp = (TextView) findViewById(R.id.temperatureVille);
            temp.setText(meteoData.getTempCelcius());
            ImageView image = (ImageView) findViewById(R.id.logo);
            image.setImageResource(meteoData.getIdPicture());
            TextView description = (TextView) findViewById(R.id.descriptionVille);
            description.setText(meteoData.getDescription());
        }else{
            Toast.makeText(getApplicationContext(), (R.string.erreur_recup_donnees), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void launchSearchTask() {
        CurrentWeatherTask searchTask = new CurrentWeatherTask();
        searchTask.execute(laVille.getNom(), getBaseContext().getString(R.string.langue_API));

        try{
            MeteoData meteoData = retrieveFromCache(laVille.getNom());
            if(null == meteoData || meteoData.isObsolete()){
                meteoData = searchTask.get();
            }
            laVille.setMeteoData(meteoData);
            saveInCache(laVille.getNom(), meteoData);
        }catch (Exception e){
            Log.e("VilleActivity", "Erreur lors de l'exécution de la tâche asynchrone", e);
        }
    }*/

    /**
     * Sauvegarde des données
     */
    public void saveInCache(String nomVille, MeteoData meteoData){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File file = new File(getCacheDir(), nomVille);
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(meteoData);
            oos.flush();
        }
        catch (FileNotFoundException fnfe) {
            Log.e("VilleActivity", "FileNotFoundException", fnfe);
        }
        catch (IOException ioe) {
            Log.e("VilleActivity", "IOException", ioe);
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
            }
            catch (IOException ioe) {
                Log.e("VilleActivity", "IOException", ioe);
            }
        }
    }


    public MeteoData retrieveFromCache(String nomVille) {
        FileInputStream fis = null;
        byte[] buffer = new byte[100];
        MeteoData meteoData = null;
        File file = new File(getCacheDir(), nomVille);
        if(file.exists()) {
            try {
                fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                meteoData = (MeteoData) ois.readObject();
            } catch (FileNotFoundException fnfe) {
                Log.e("VilleActivity", "FileNotFoundException", fnfe);
            } catch (IOException ioe) {
                Log.e("VilleActivity", "IOException", ioe);
            } catch (ClassNotFoundException cnfe) {
                Log.e("VilleActivity", "ClassNotFoundException", cnfe);
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (IOException ioe) {
                    Log.e("VilleActivity", "IOException", ioe);
                }
            }
        }
        return meteoData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ville, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_apropos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("@string/aPropos")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
