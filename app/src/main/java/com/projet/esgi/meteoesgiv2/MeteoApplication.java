package com.projet.esgi.meteoesgiv2;

import android.app.Application;
import android.content.SharedPreferences;

import com.projet.esgi.meteoesgiv2.modele.Ville;

import java.util.ArrayList;

public class MeteoApplication extends Application{
    private static ArrayList<Ville> lesVilles = new ArrayList<Ville>();
    private static ArrayList<Ville> lesVillesFavoris = new ArrayList<Ville>();
    private static final String PREF_FILE_NAME = "prefs";

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
    }
    public void initListeVillesFavoris() {
        lesVillesFavoris.clear();
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        for (Ville v : lesVilles){
            if(pref.contains(v.getNom())){
                v.setFavoris(true);
                lesVillesFavoris.add(v);
            }
            else{
                v.setFavoris(false);
            }
        }
    }

    public ArrayList<Ville> getLesVilles(){
        return lesVilles;
    }
    public ArrayList<Ville> getLesVillesFavoris(){
        return lesVillesFavoris;
    }

    public void addFavoris(Ville v){
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        pref.edit().putString(v.getNom(),v.getNom()).commit();
        initListeVillesFavoris();
    }

    public void removeFavoris(Ville v){
        SharedPreferences pref = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        pref.edit().remove(v.getNom()).commit();
        initListeVillesFavoris();
    }
}
