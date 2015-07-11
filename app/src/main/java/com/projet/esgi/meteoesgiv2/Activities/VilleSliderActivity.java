package com.projet.esgi.meteoesgiv2.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.projet.esgi.meteoesgiv2.Fragments.VilleSlideFragment;
import com.projet.esgi.meteoesgiv2.MeteoAPI.FiveDaysWeatherTask;
import com.projet.esgi.meteoesgiv2.R;
import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;


public class VilleSliderActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Ville laVille = new Ville();

    private static final String FICHIER_CACHE = "cache_donnees_meteo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville_slider);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        Intent i = getIntent();
        laVille = (Ville)i.getSerializableExtra("ville");

        launchSearchTask();
    }

    private void launchSearchTask() {
        //test la connexion
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        try{
            if(isConnected) {
                FiveDaysWeatherTask searchTask = new FiveDaysWeatherTask();
                searchTask.execute(laVille.getNom(), getApplicationContext().getString(R.string.langue_API));
                MeteoData[] meteoData = searchTask.get();
                laVille.setMeteoData(meteoData);
                saveInCache(laVille.getNom(), meteoData);
            }else {
                MeteoData[] meteoData = retrieveFromCache(laVille.getNom());
                if(null != meteoData ){
                    if(!meteoData[0].isObsolete())
                        laVille.setMeteoData(meteoData);
                }else{
                    Toast.makeText(this, (R.string.erreur_connexion), Toast.LENGTH_SHORT).show();
                    finish();
                    onBackPressed();
                }
            }

        }catch (Exception e){
            Log.e("VilleSliderActivity", "Erreur lors de l'exécution de la tâche asynchrone", e);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * Envoie les arguments au Fragment
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return VilleSlideFragment.newInstance(laVille, 0);
                case 1:
                    return VilleSlideFragment.newInstance(laVille, 1);
                case 2:
                    return VilleSlideFragment.newInstance(laVille, 2);
                case 3:
                    return VilleSlideFragment.newInstance(laVille, 3);
                case 4:
                    return VilleSlideFragment.newInstance(laVille, 4);
                default:
                    return new VilleSlideFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sauvegarde des données
     */
    public void saveInCache(String nomVille, MeteoData[] meteoData){
        meteoData[0].setDate(new Date().getTime());
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


    public MeteoData[] retrieveFromCache(String nomVille) {
        FileInputStream fis = null;
        byte[] buffer = new byte[100];
        MeteoData[] meteoData = null;
        File file = new File(getCacheDir(), nomVille);
        if(file.exists()) {
            try {
                fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                meteoData = (MeteoData[]) ois.readObject();
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
}