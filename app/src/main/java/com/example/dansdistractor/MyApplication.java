package com.example.dansdistractor;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.type.DateTime;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication singleton;

    private List<Location> myLocations = null;

    private List<LatLng> targetLocations = null;

    private List<LatLng> completedTargetLocations = null;

    private Boolean sessionStarted = false;
    private Boolean sessionPaused = false;
    private int stepCount = 0;
    private boolean hasInitialStepCount = false;
    private int initialStepCount = 0;

//    private LocalDate startDate;
//    private LocalTime startTime;
//    private LocalDate endDate;
//    private LocalTime endTime;

    private Date startDate;
    private Date endDate;

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public boolean isHasInitialStepCount() {
        return hasInitialStepCount;
    }

    public void setHasInitialStepCount(boolean hasInitialStepCount) {
        this.hasInitialStepCount = hasInitialStepCount;
    }

    public int getInitialStepCount() {
        return initialStepCount;
    }

    public void setInitialStepCount(int initialStepCount) {
        this.initialStepCount = initialStepCount;
    }

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
        stepCount = 0;
        hasInitialStepCount = false;
        initialStepCount = 0;
        startDate = new java.util.Date();
        Log.i("datetime", "startdate: " + startDate.toString());
    }

    protected void endSession(){
        sessionStarted = false;
        hasInitialStepCount = false;
        endDate = new java.util.Date();
        Log.i("datetime", "enddate: " + endDate.toString());
    }

    public void onCreate(){
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        completedTargetLocations = new ArrayList<>();
        stepCount = 0;
        hasInitialStepCount = false;
        initialStepCount = 0;
    }
}
