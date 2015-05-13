package com.mawsom.mawsom.mawsomnobl;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Waqas on 5/8/2015.
 */
public class Location implements LocationListener {

    private LocationManager locationManager;
    private String provider;

    @Override
    public void onLocationChanged(android.location.Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
