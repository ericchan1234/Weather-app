package hk.edu.ouhk.weather;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class WeatherAPI extends Thread {
    private double Longitude;
    private double Latitude;
    private LocationManager lm;
    private static String TAG = "Weather APIS";
    private String apikey = "2236d5671cae671520f77956e5350c1a";

    public WeatherAPI(double lng, double lat){
        Longitude = lng;
        Latitude = lat;
    }


    public String MakeRequest(double lat, double lng) throws IOException{
        String response = null;


        String JsonUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + apikey;
        Log.d(TAG, "Running Request");
        try{
            URL url = new URL(JsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = inputStreamToString(in);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String inputStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try{
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch(IOException e){
            Log.e(TAG, "IO EXCEPTION: " + e.getMessage());
        } finally {
            try{
                in.close();
            } catch(IOException ioe) {
                Log.e(TAG, "IO EXCEPTION: " + ioe.getMessage());
            }
        }
        return sb.toString();
    }

    @SuppressLint("MissingPermission")


    public void run(){
        try {
            Log.d(TAG, String.valueOf(Longitude));
            Log.d(TAG, String.valueOf(Latitude));
            String result = MakeRequest(Latitude, Longitude);

            Log.d(TAG, "Response from url: " + result);

            if (result != null) {
                try {
                    JSONObject JsonObj = new JSONObject(result);
                    JSONObject main = JsonObj.getJSONObject("main");
                    String currtemp = main.getString("temp");
                    String feels_like = main.getString("feels_like");
                    String temp_min = main.getString("temp_min");
                    String temp_max = main.getString("temp_max");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    JSONObject wind = JsonObj.getJSONObject("wind");
                    String windspeed = wind.getString("speed");
                    String winddegree = wind.getString("deg");

                    String location = JsonObj.getString("name");
                    String dt = JsonObj.getString("dt");
                    String timezone = JsonObj.getString("timezone");
                    Log.d(TAG, "Running Thread");
                    Weather.WeatherStats(currtemp, feels_like, temp_min, temp_max, pressure, humidity, windspeed, winddegree, location, dt, timezone);
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
