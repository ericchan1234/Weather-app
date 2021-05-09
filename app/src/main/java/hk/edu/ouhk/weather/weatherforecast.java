package hk.edu.ouhk.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class weatherforecast extends AppCompatActivity implements LocationListener{

    private Integer[] images = { R.drawable.day, R.drawable.night };
    public View fframe;
    public Button homepageBtn;
    public ListView listView;
    public TextView location;
    private String[] Weatherid = { "01d", "01n", "02d", "02n", "03d", "03n", "04d", "04n", "09d", "09n", "10d", "10n", "11d", "11n", "13d", "13n", "50d", "50n"};
    private Integer[] Weatherimages = { R.drawable.a01d, R.drawable.a01n, R.drawable.a02d, R.drawable.a02n, R.drawable.a03d, R.drawable.a03n, R.drawable.a04d, R.drawable.a04n, R.drawable.a09d, R.drawable.a09n, R.drawable.a10d, R.drawable.a10n, R.drawable.a11d, R.drawable.a11n, R.drawable.a13d, R.drawable.a13n, R.drawable.a50d, R.drawable.a50n};

    public boolean permission = false;
    private double Longitude = 0;
    private double Latitude = 0;

    public static boolean isCelsius = true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherforecast);
        Bundle extras = getIntent().getExtras();
        isCelsius = extras.getBoolean("isCelsius");

        homepageBtn = findViewById(R.id.button);
        fframe = (View) findViewById(R.id.forecastframe);
        listView = (ListView) findViewById(R.id.forecastlist);
        location = findViewById(R.id.location2);
        homepageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(weatherforecast.this, MainActivity.class));
            }
        });

        while (!permission) {
            checkpermission();
        }

        ForecastAPI fapi = new ForecastAPI(Longitude, Latitude);
        FWeather.clearData();
        fapi.start();

        try {
            fapi.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, FWeather.foreweather, R.layout.activity_list_view_forecast, new String[]{ FWeather.date, FWeather.temp}, new int[]{R.id.fdate, R.id.ftemp}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ImageView i = (ImageView) v.findViewById(R.id.weatherimage);
                for(int j = 0; j < Weatherid.length; j ++){
                    if(Weatherid[j].equals(FWeather.foreweather.get(position).get("Icon"))){
                        i.setImageResource(Weatherimages[j]);
                    }
                }
                return v;
            }
        };

        listView.setAdapter(adapter);

        location.setText("7-day Forecast(" + Weather.getLocation() + ")");


        if(Weather.getCurrHour() < 6 || Weather.getCurrHour() >= 18){
            fframe.setBackgroundResource(images[1]);
        } else {
            fframe.setBackgroundResource(images[0]);
        }
        getSupportActionBar().hide();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)

    public boolean checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
            getLocationManager();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    public void getLocationManager() {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        Location location = null;
        try{
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps_enabled && !network_enabled) {

            } else{
                if(network_enabled){
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    if(lm != null){
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            Longitude = location.getLongitude();
                            Latitude = location.getLatitude();
                        }
                    }
                }
                if(gps_enabled){
                    if(location == null){
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                        if(lm != null){
                            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location != null){
                                Longitude = location.getLongitude();
                                Latitude = location.getLatitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onLocationChanged(@NonNull Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
    }

}