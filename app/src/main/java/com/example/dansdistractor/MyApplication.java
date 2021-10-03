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

    private List<LatLng> completedTargetLocations = null;

    private Boolean sessionStarted = false;
    private Boolean sessionPaused = false;

    public List<LatLng> getCompletedTargetLocations() {
        return completedTargetLocations;
    }

    public void setCompletedTargetLocations(List<LatLng> completedTargetLocations) {
        this.completedTargetLocations = completedTargetLocations;
    }

    protected Boolean getSessionPaused(){
        return sessionPaused;
    }

    protected void setSessionPaused(Boolean sessionPaused){
        this.sessionPaused = sessionPaused;
    }

    protected Boolean getSessionStarted(){
        return sessionStarted;
    }

    protected void setSessionStarted(Boolean sessionStarted){
        this.sessionStarted = sessionStarted;
    }

    protected List<LatLng> getTargetLocations() {
        return targetLocations;
    }

    protected void setTargetLocations(List<LatLng> targetLocations) {
        this.targetLocations = targetLocations;
    }

    protected List<Location> getMyLocations() {
        return myLocations;
    }

    protected void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    protected MyApplication getInstance(){
        return singleton;
    }

    protected void startSession(){
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        sessionStarted = true;
        completedTargetLocations = new ArrayList<>();
    }

    protected void endSession(){
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        sessionStarted = false;
        completedTargetLocations = new ArrayList<>();
    }

    public void onCreate(){
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        completedTargetLocations = new ArrayList<>();
    }
}
