package com.example.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocationManager {

    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60; // 1 minute
    private static final float MIN_DISTANCE_FOR_UPDATES = 10.0f; // 10 meters

    private Context mContext;
    private LocationManager mLocationManager;
    private MyLocationListener mLocationListener;

    private double currentLatitude;
    private double currentLongitude;
    public MyLocationManager(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
    }

    public void requestLocationUpdates() {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_FOR_UPDATES,
                    mLocationListener
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void removeUpdates() {
        try {
            mLocationManager.removeUpdates(mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // Handle location updates here
            currentLatitude = location.getLatitude();
            currentLongitude= location.getLongitude();
            Log.d("currentLat",""+currentLatitude);
            Log.d("currentLong",""+currentLongitude);

            // Do something with latitude and longitude, for example, log or update UI
            // You can also broadcast an intent or use a callback to inform other parts of your app
            // about the updated location.
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle status changes if needed
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Handle provider enabled
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Handle provider disabled
        }
    }
}
