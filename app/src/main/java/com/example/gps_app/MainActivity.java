package com.example.gps_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private TextView tvRest, tvTotal,tvVelosity;
    private Location lastlocation;
    private MyLocListener myLocListener;
    private  int distance;
    private ProgressBar progressBar;
    private int disTotal;
    private int disRest;
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
        tvTotal = findViewById(R.id.tvTotal);
        tvRest = findViewById(R.id.tvRest);
        tvVelosity = findViewById(R.id.tvVelosity);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(1000);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyLocListener();
        myLocListener.setLocListenerInterface(this);
        checkPermitions();
    }

    private void updateDistance(Location location){
        if(location.hasSpeed() && lastlocation != null){
            if(distance>disTotal){
                disTotal+= lastlocation.distanceTo(location);
            }
            if (disRest> 0){
                disRest-= lastlocation.distanceTo(location);
            }
            progressBar.setProgress(disTotal);
        }
        lastlocation = location;
        tvRest.setText(String.valueOf(disRest));
        tvTotal.setText(String.valueOf(disTotal));
        tvVelosity.setText(String.valueOf(location.getSpeed()));
    }

    private  void setDistance(String dis){
        progressBar.setMax(Integer.parseInt(dis));
        disRest = Integer.parseInt(dis);
        distance = Integer.parseInt(dis);

        tvRest.setText(dis);
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.set_goal_distance,null);
        builder.setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;
                EditText ed = ad.findViewById(R.id.editTextText);
                if(ed != null){
                    if(!ed.getText().toString().equals("")){
                        setDistance(ed.getText().toString());
                    }
                }
            }
        });
        builder.setView(cl);

        builder.show();
    }

    public void onClickSetDistance(View view){
        showDialog();
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
        updateDistance(location);
    }
}