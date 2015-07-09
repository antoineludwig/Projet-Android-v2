package com.projet.esgi.meteoesgiv2.MeteoAPI;

import com.projet.esgi.meteoesgiv2.modele.MeteoData;
import com.projet.esgi.meteoesgiv2.modele.Ville;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static MeteoData[] parseFiveDaysWeatherData(String json) throws JSONException {

        if (json != null) {
            Ville ville = new Ville();
            int conteur=0;

            MeteoData[] lesdatas = new MeteoData[5];

            Calendar currentDate = Calendar.getInstance();
            Calendar dateJSON = Calendar.getInstance();


            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonCity = jsonObject.getJSONObject("city");

            ville.setNom(jsonCity.getString("name"));

            JSONObject jsonCoord = jsonCity.getJSONObject("coord");
            ville.setLatitude(jsonCoord.getDouble("lat"));
            ville.setLongitude(jsonCoord.getDouble("lon"));


            JSONArray arrayListWeather = jsonObject.getJSONArray("list");
            for (int i = 0; i < arrayListWeather.length(); ++i) {
                MeteoData meteoData = new MeteoData();
                JSONObject list = arrayListWeather.getJSONObject(i);
                String dateInString = list.getString("dt_txt");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    dateJSON.setTime(formatter.parse(dateInString));
                } catch (ParseException e) {e.printStackTrace();}

                //si la date courante est comprise dans la fourchette (1 donnée toute les 3 heures)
                if(currentDate.get(Calendar.HOUR_OF_DAY)-dateJSON.get(Calendar.HOUR_OF_DAY)>-2 &&
                        currentDate.get(Calendar.HOUR_OF_DAY)-dateJSON.get(Calendar.HOUR_OF_DAY)<2){

                    meteoData.setDatePrevision(dateInString);

                    JSONArray arrayWeather = list.getJSONArray("weather");
                    meteoData.setId(arrayWeather.getJSONObject(0).getInt("id"));
                    meteoData.setDescription(arrayWeather.getJSONObject(0).getString("description"));

                    JSONObject jsonMain = list.getJSONObject("main");
                    meteoData.setTemp(jsonMain.getDouble("temp"));
                    meteoData.setHumidity(jsonMain.getDouble("humidity"));
                    meteoData.setTemp_min(jsonMain.getDouble("temp_min"));
                    meteoData.setTemp_max(jsonMain.getDouble("temp_max"));
                    meteoData.setPressure(jsonMain.getDouble("pressure"));

                    JSONObject jsonWind = list.getJSONObject("wind");
                    meteoData.setWindSpeed(jsonWind.getDouble("speed"));
                    meteoData.setWindDirection(jsonWind.getDouble("deg"));

                    lesdatas[conteur]=meteoData;
                    conteur++;
                }
            }

            ville.setMeteoData(lesdatas);

            return lesdatas;
        }else{
            return null;
        }
    }

}
