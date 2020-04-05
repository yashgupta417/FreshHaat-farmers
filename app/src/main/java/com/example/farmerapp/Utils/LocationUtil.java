package com.example.farmerapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.Activities.RegisterDetailsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil{
    LocationManager locationManager = null;
    LocationListener locationListener = null;
    Location location;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    Application application;
    public boolean has_permission=false;
    MutableLiveData<List<Address>> address;
    public LocationUtil(Application application) {
        this.application=application;
        address=new MutableLiveData<List<Address>>();
        locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        initializeLocationListener();
        getLocation();
    }
    public LiveData<List<Address>> observeAddress(){
        return address;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (application.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && application.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
                return true;
        }
    }
    public Boolean isLocationEnabled(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }
    private void getLocation() {
        if(!checkPermission()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!isLocationEnabled(application)) {
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null) {
            updateAddress(location);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_FOR_UPDATE,MIN_DISTANCE_FOR_UPDATE, locationListener);
        }
    }
    private void updateAddress(Location location){
        if(location==null)
            return;
        Geocoder geocoder = new Geocoder(application, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setValue(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeLocationListener(){
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateAddress(location);
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
        };
    }

}
