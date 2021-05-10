package hk.edu.ouhk.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RequestPermission extends AppCompatActivity {
    public double Longitude = 0;
    public double Latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        getSupportActionBar().hide();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = true;
        for (int i = 0; i < grantResults.length; i++){
            if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {
                isGranted = false;
            }
        }
        if(!isGranted){
            finish();
        } else{
            checkpermission();
        }
        return;

    }

    public void getLocationManager() {
        boolean gps_enabled = false;
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps_enabled) {
                startActivity(new Intent(RequestPermission.this, MainActivity.class)); // Start new Activity
            } else{
                finish();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkpermission() {
        // Check Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationManager();
        }
    }

}