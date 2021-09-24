package com.example.dansdistractor;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication singleton;

    private List<Location> myLocations = null;

    private List<LatLng> targetLocations = null;

    public List<LatLng> getTargetLocations() {
        return targetLocations;
    }

    public void setTargetLocations(List<LatLng> targetLocations) {
        this.targetLocations = targetLocations;
    }

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public MyApplication getInstance(){
        return singleton;
    }

    public void onCreate(){
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
    }
}
