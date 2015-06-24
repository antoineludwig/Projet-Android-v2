package com.projet.esgi.meteoesgiv2.MeteoAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.projet.esgi.meteoesgiv2.modele.MeteoData;


public class CurrentWeatherTask extends AsyncTask<String, Void, MeteoData> {


    @Override
    protected MeteoData doInBackground(String... params) {
        MeteoData meteoData = null;
        String rsltRequete = "";
        try {
            RequeteurAPI requeteurAPI = new RequeteurAPI();
            rsltRequete = requeteurAPI.queryCurrentWeather(params[0], params[1]);

            if(!rsltRequete.equals(""))
                meteoData = ParserJSON.parseCurrentWeatherData(rsltRequete);
        } catch (Exception e) {
            Log.e("CurrentWeatherTask", "Erreur lors de la tâche de récupération et parsage des données Météo", e);
        }
        return meteoData;
    }

}
