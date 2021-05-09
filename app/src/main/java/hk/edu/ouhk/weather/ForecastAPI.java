package hk.edu.ouhk.weather;

import android.location.LocationManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastAPI extends Thread {
    private double Longitude;
    private double Latitude;
    private String apikey = "14986c24d97b00cb6f239a24f4f42044";
    private static String TAG = "Forecast API";
    private LocationManager lm;

    public ForecastAPI(double lng, double lat){
        Longitude = lng;
        Latitude = lat;
    }

    public String MakeRequest(double lat, double lng) throws IOException {
        String response = null;
        String JsonUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lng + "&appid=" + apikey;

        try {
            URL url = new URL(JsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = inputStreamToString(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String inputStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "IO EXCEPTION: " + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                Log.e(TAG, "IO EXCEPTION: " + ioe.getMessage());
            }
        }
        return sb.toString();
    }


    public void run() {
        try {
            String result = MakeRequest(Latitude, Longitude);

            Log.d(TAG, "Response from url: " + result);

            if (result != null) {
                try {
                    JSONObject JsonObj = new JSONObject(result);
                    JSONArray list = JsonObj.getJSONArray("list");
                    for(int i = 0; i < list.length(); i ++){
                        JSONObject w = list.getJSONObject(i);
                        String dt = w.getString("dt");

                        JSONObject temp = w.getJSONObject("temp");
                        String temp_min = temp.getString("min");
                        String temp_max = temp.getString("max");

                        String icon = "";
                        JSONArray weather = w.getJSONArray("weather");
                        for(int j = 0; j < weather.length(); j ++){
                            JSONObject we = weather.getJSONObject(j);
                            icon = we.getString("icon");
                        }


                        FWeather.addWeatherData(dt, temp_min, temp_max, icon);
                    }
                    Log.d(TAG, "Running Thread");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}