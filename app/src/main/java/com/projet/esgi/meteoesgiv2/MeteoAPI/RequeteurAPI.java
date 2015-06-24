package com.projet.esgi.meteoesgiv2.MeteoAPI;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequeteurAPI {
    private static String API_KEY = "19bfba1b5dab9c42b41b280033cb9c90";
    private static String URL_CURRENT_WHEATHER = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private static String CHARSET = "UTF-8";

    private String buildParamVille(String nomVille) throws UnsupportedEncodingException {
        return String.format("q=%s",
                URLEncoder.encode(nomVille, CHARSET));
    }
    private String buildParamLangue(String langue) throws UnsupportedEncodingException {
        return String.format("lang=%s",
                URLEncoder.encode(langue, CHARSET));
    }

    public String queryCurrentWeather(String nomVille, String langue){
        String reponse = "";
        HttpURLConnection connection = null;
        InputStream iStream = null;
        try {
            String paramVille = this.buildParamVille(nomVille);
            String paramLangue = this.buildParamLangue(langue);

            String requete = String.format("%s&%s&%s", URL_CURRENT_WHEATHER, paramVille, paramLangue);

            connection = (HttpURLConnection) new URL(requete).openConnection();
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            iStream = connection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));

            String line;
            StringBuffer sBuffer = new StringBuffer();
            while ((line = bReader.readLine()) != null){
                sBuffer.append(line);
            }
            iStream.close();
            connection.disconnect();

            reponse = sBuffer.toString();
        } catch (Exception e) {
            Log.e("RequeteurAPI", "Erreur lors de la récupération des données", e);
        }

        return reponse;
    }
}
