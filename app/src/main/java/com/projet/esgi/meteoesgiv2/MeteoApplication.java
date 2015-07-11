package com.projet.esgi.meteoesgiv2;

import android.app.Application;
import android.content.SharedPreferences;

import com.projet.esgi.meteoesgiv2.modele.Ville;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MeteoApplication extends Application{
    private static ArrayList<Ville> lesVilles = new ArrayList<Ville>();
    private static ArrayList<Ville> lesVillesFavoris = new ArrayList<Ville>();
    private static final String PREF_FILE_NAME_VILLE = "villes";
    private static final String PREF_FILE_NAME_FAV = "favs";

    @Override
    public void onCreate() {
        super.onCreate();
        initListeVilles();
        initListeVillesFavoris();
    }

    private void initListeVilles() {
        lesVilles.add(new Ville("London"));
        lesVilles.add(new Ville("Paris"));
        lesVilles.add(new Ville("Madrid"));
        lesVilles.add(new Ville("Berlin"));
        lesVilles.add(new Ville("Lisbon"));
        lesVilles.add(new Ville("Miami"));
        lesVilles.add(new Ville("Valognes"));
        lesVilles.add(new Ville("Montataire"));
        lesVilles.add(new Ville("Moscow"));
        lesVilles.add(new Ville("Stockholm"));
        lesVilles.add(new Ville("Caen"));
        lesVilles.add(new Ville("Versailles"));
        lesVilles.add(new Ville("Copenhague"));
        lesVilles.add(new Ville("Camberra"));
        lesVilles.add(new Ville("Alger"));
        lesVilles.add(new Ville("Chicago"));
        lesVilles.add(new Ville("Toronto"));
        lesVilles.add(new Ville("Istanbul"));
        lesVilles.add(new Ville("Athena"));
        lesVilles.add(new Ville("Kiev"));
        lesVilles.add(new Ville("Detroit"));

        File f = new File("/data/data/"+getApplicationContext().getPackageName()+"/shared_prefs"+"/villes.xml");
        if(!f.exists()){
            for(Ville v : lesVilles){
                addVille(v);
            }
        }


        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_VILLE, MODE_PRIVATE);
        Map<String,?> keys = pref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            lesVilles.add(new Ville(entry.getValue().toString()));
        }
    }

    public void initListeVillesFavoris() {
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_FAV, MODE_PRIVATE);
        Map<String,?> keys = pref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Ville v = new Ville(entry.getValue().toString());
            v.setFavoris(true);
            lesVillesFavoris.add(v);
        }
    }

    public ArrayList<Ville> getLesVilles(){
        return lesVilles;
    }
    public ArrayList<Ville> getLesVillesFavoris(){
        return lesVillesFavoris;
    }

    public void addFavoris(Ville v){
        lesVillesFavoris.clear();
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_FAV, MODE_PRIVATE);
        pref.edit().putString(v.getNom(),v.getNom()).commit();
        initListeVillesFavoris();
    }

    public void removeFavoris(Ville v){
        lesVillesFavoris.clear();
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_FAV, MODE_PRIVATE);
        pref.edit().remove(v.getNom()).commit();
        initListeVillesFavoris();
    }

    public void addVille(Ville v){
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_VILLE, MODE_PRIVATE);
        pref.edit().putString(v.getNom(),v.getNom()).commit();
        //initListeVillesFavoris();
    }

    public void removeVille(Ville v){
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME_VILLE, MODE_PRIVATE);
        pref.edit().remove(v.getNom()).commit();
        //initListeVillesFavoris();
    }
}
