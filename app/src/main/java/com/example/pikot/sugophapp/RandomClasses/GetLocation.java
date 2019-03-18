package com.example.pikot.sugophapp.RandomClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import org.json.JSONException;

import static android.content.Context.LOCATION_SERVICE;

public class GetLocation implements LocationListener{
    private interface locationCallback{
        void onSuccess(String response);
    }
    LocationManager locationManager;
    Activity activity;
    String currentLocation;

    public GetLocation(Activity activity) {
        this.activity= activity;
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }
    public String getCurrentLocation(){

        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation= location.getLatitude()+"-"+location.getLongitude();
        Toast.makeText(activity, currentLocation, Toast.LENGTH_SHORT).show();
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



//    public void getLocation(final CallBacks callBacks){
//
//        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                currentLocation= location.getLatitude()+"-"+location.getLongitude();
//                try {
//                    callBacks.onSuccess(currentLocation);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        }
//    }
}
