package com.mawsom.mawsom.mawsomnobl;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Waqas on 5/13/2015.
 */
public class locationManager implements LocationListener {

    private LocationManager locationManager;
    private LocationListener locationListener;
    protected Context context;
    String latitude;
    String longitude;
    boolean gpsOn, networkOn;
    String provider;

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Latitude", "enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        
    }
}
