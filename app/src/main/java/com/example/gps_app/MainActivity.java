package com.example.gps_app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements LocListenerInterface{
    private LocationManager locationManager;
    private TextView tvdistance,tvVelocity;
    private Location lastlocation;
    private MyLocListener myLocListener;
    private  int distance;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }


    //TODO Check sintaksis
    private  void init(){
        tvVelocity = findViewById(R.id.tvVelosity);
        tvdistance = findViewById(R.id.tvdistance);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(1000);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermitions();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        //ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate()
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if(requestCode == 100 && grantResults[0] == RESULT_OK){
            checkPermitions();
        }
        /*else{
            Toast.makeText(this,"No GPS permition",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void checkPermitions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},102);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,1,myLocListener);
        }
    }

    @Override
    public void OnLocztionChange(Location location) {
        if(location.hasSpeed() && lastlocation != null){
            distance+= lastlocation.distanceTo(location);
        }
        lastlocation = location;
        tvdistance.setText(String.valueOf(distance));
        tvVelocity.setText(String.valueOf(location.getSpeed()));
    }
}