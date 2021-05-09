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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public TextView locationText;
    public TextView date;
    public TextView time;
    public TextView temp;
    public TextView humidity;
    public TextView feels_like;
    public TextView temp_min;
    public TextView temp_max;
    public View mainframe;
    private Integer[] images = {R.drawable.day, R.drawable.night};
    public TextView updatedTime;
    public Button forecastBtn;
    public Button conversionButton;

    public boolean isCelsius = true;
    public boolean permission = false;
    private double Longitude = 0;
    private double Latitude = 0;
    protected LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainframe = findViewById(R.id.mainframe);
        locationText = findViewById(R.id.location);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        temp = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        feels_like = findViewById(R.id.feels_like);
        temp_min = findViewById(R.id.temp_min);
        temp_max = findViewById(R.id.temp_max);
        updatedTime = findViewById(R.id.updatedtime);
        forecastBtn = (Button) findViewById(R.id.button2);
        conversionButton = (Button) findViewById(R.id.conversionButton);
        if (!permission) {
            checkpermission();
        }
        if(isCelsius) {
            conversionButton.setText("°C");
        } else {
            conversionButton.setText("°F");
        }
        WeatherAPI weatherAPI = new WeatherAPI(Longitude, Latitude);
        weatherAPI.start();
        try {
            weatherAPI.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    WeatherAPI weatherAPI = new WeatherAPI(Longitude, Latitude);
                    weatherAPI.start();

                    locationText.setText(Weather.getLocation());
                    date.setText(Weather.getDate());
                    time.setText(Weather.getCurrTime());
                    updatedTime.setText("Last update: " + Weather.getTime());

                    humidity.setText(Weather.getHumidity() + "%");

                    if(!isCelsius){
                        feels_like.setText("Feels: " + String.valueOf(Double.valueOf(Weather.getFeels_like()) * 1.8 + 32) + "°F");
                        temp.setText(String.valueOf(Double.valueOf(Weather.getTemp()) * 1.8 + 32) + "°F");
                        temp_min.setText("▼" + String.valueOf(Double.valueOf(Weather.getTemp_min()) * 1.8 + 32) + "°F");
                        temp_max.setText("▲" + String.valueOf(Double.valueOf(Weather.getTemp_max()) * 1.8 + 32) + "°F");

                    } else{
                        feels_like.setText("Feels: " + Weather.getFeels_like() + "°C");
                        temp.setText(Weather.getTemp() + "°C");
                        temp_min.setText("▼" + Weather.getTemp_min() + "°C");
                        temp_max.setText("▲" + Weather.getTemp_max() + "°C");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000);
        locationText.setText(Weather.getLocation());
        date.setText(Weather.getDate());
        time.setText(Weather.getCurrTime());
        updatedTime.setText(Weather.getTime());

        humidity.setText(Weather.getHumidity() + "%");

        feels_like.setText("Feels: " + Weather.getFeels_like() + "°C");
        temp.setText(Weather.getTemp() + "°C");
        temp_min.setText("▼" + Weather.getTemp_min() + "°C");
        temp_max.setText("▲" + Weather.getTemp_max() + "°C");

        if (Weather.getCurrHour() < 6 || Weather.getCurrHour() >= 18) {
            mainframe.setBackgroundResource(images[1]);
        } else {
            mainframe.setBackgroundResource(images[0]);
        }

        forecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                startActivity(new Intent(MainActivity.this, weatherforecast.class).putExtra("isCelsius", isCelsius));
            }
        });
        conversionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCelsius){
                    feels_like.setText("Feels: " + ConvertCTOF(Weather.getFeels_like()) + "°F");
                    temp.setText(ConvertCTOF(Weather.getTemp()) + "°F");
                    temp_min.setText("▼" + ConvertCTOF(Weather.getTemp_min()) + "°F");
                    temp_max.setText("▲" + ConvertCTOF(Weather.getTemp_max()) + "°F");
                    conversionButton.setText("°F");
                    isCelsius = false;
                } else{
                    feels_like.setText("Feels: " + Weather.getFeels_like() + "°C");
                    temp.setText(Weather.getTemp() + "°C");
                    temp_min.setText("▼" + Weather.getTemp_min() + "°C");
                    temp_max.setText("▲" + Weather.getTemp_max() + "°C");
                    isCelsius = true;
                    conversionButton.setText("°C");
                }
            }
        });
        getSupportActionBar().hide();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permission = true;
            getLocationManager();
        } else {
            finish();
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
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
    public String ConvertCTOF(String num){
        double f = Double.valueOf(num) * 1.8 +32;
        return String.valueOf((Math.floor(f*10))/ 10);
    }
}