package com.example.dansdistractor.databaseSchema;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class UserHistorySchema {

    public Date endDateTime, startDateTime;
    public int steps, pins;
    public double distance, avgSpeed;

    public List<Location> myLocations = null;
    private List<LatLng> targetLocations = null;
    private List<LatLng> completedTargetLocations = null;

    public UserHistorySchema() {
    }

    public UserHistorySchema(Date endDateTime, Date startDateTime, List<Location> myLocations, List<LatLng> targetLocations, List<LatLng> completedTargetLocations, int steps) {
        this.endDateTime = endDateTime;
        this.startDateTime = startDateTime;
        this.myLocations = myLocations;
        this.targetLocations = targetLocations;
        this.completedTargetLocations = completedTargetLocations;
        this.steps = steps;
        this.distance = getDistance();
        this.avgSpeed = getSpeed();
        this.pins = getPins();
    }

    //get distance in meters between two location instance
    private double getDistance(){

        double totalDistance = 0;

        for (int counter = 0; counter < myLocations.size()-1; counter++) {

            totalDistance += myLocations.get(counter).distanceTo(myLocations.get(counter+1));
        }

        return totalDistance;
    }

    private double getSpeed(){
        if (endDateTime.getTime() - startDateTime.getTime() == 0) return 0;
        return distance/endDateTime.getTime() - startDateTime.getTime();
    }

    private int getPins(){
        return completedTargetLocations.size();
    }







}
