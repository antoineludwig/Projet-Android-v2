package com.projet.esgi.meteoesgiv2.MeteoAPI;

import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParserJSON {

    public static MeteoData parseCurrentWeatherData(String json) throws JSONException {
        if (json != null) {
            Ville ville = new Ville();

            JSONObject jsonObject = new JSONObject(json);
            ville.setNom(jsonObject.getString("name"));

            JSONObject jsonCoord = jsonObject.getJSONObject("coord");
            ville.setLatitude(jsonCoord.getDouble("lat"));
            ville.setLongitude(jsonCoord.getDouble("lon"));

            MeteoData meteoData = new MeteoData();

            JSONArray arrayWeather = jsonObject.getJSONArray("weather");
            meteoData.setId(arrayWeather.getJSONObject(0).getInt("id"));
            meteoData.setDescription(arrayWeather.getJSONObject(0).getString("description"));

            JSONObject jsonMain = jsonObject.getJSONObject("main");
            meteoData.setTemp(jsonMain.getDouble("temp"));
            meteoData.setHumidity(jsonMain.getDouble("humidity"));
            meteoData.setTemp_min(jsonMain.getDouble("temp_min"));
            meteoData.setTemp_max(jsonMain.getDouble("temp_max"));
            meteoData.setPressure(jsonMain.getDouble("pressure"));

            JSONObject jsonWind = jsonObject.getJSONObject("wind");
            meteoData.setWindSpeed(jsonWind.getDouble("speed"));
            meteoData.setWindDirection(jsonWind.getDouble("deg"));

            ville.setMeteoData(meteoData);

            return meteoData;
        }else{
            return null;
        }
    }

}
