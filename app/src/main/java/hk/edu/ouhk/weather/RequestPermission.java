package hk.edu.ouhk.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RequestPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        getSupportActionBar().hide();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkpermission();
            } else {
                Toast.makeText(this, "Please Allow GPS Permission", Toast.LENGTH_LONG).show();
                finish();
            }
            return;

    }
    public void getLocationManager() {
        boolean gps_enabled = false;
        try{
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
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