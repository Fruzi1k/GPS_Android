package com.example.gps_app;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.util.List;

public class MyLocListener implements LocationListener {
    private LocListenerInterface locListenerInterface;

    public void setLocListenerInterface(LocListenerInterface locListenerInterface) {
        this.locListenerInterface = locListenerInterface;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locListenerInterface.OnLocztionChange(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }
}
