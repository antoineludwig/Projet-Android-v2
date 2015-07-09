package com.projet.esgi.meteoesgiv2.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ville implements Serializable {
    private String nom;
    private double longitude;
    private double latitude;
    private boolean favoris;

    private MeteoData meteoData;

    private MeteoData[] lesdatas;

    public Ville(){
        lesdatas = new MeteoData[4];
    }

    public Ville(String _nom){
        nom=_nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public MeteoData getMeteoData() {
        return meteoData;
    }

    public void setMeteoData(MeteoData meteoData) {
        this.meteoData = meteoData;
    }

    public boolean isFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    public MeteoData getMeteoData(int jour) {
        return lesdatas[jour];
    }

    public void setMeteoData(MeteoData[] meteoDatas) {
        this.lesdatas = meteoDatas;
    }
}
