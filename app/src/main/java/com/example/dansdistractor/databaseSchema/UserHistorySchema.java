package com.example.dansdistractor.databaseSchema;

import com.example.dansdistractor.utils.MyLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import java.sql.Time;
import java.util.Date;
import java.util.List;

public class UserHistorySchema {

    public Date endDateTime, startDateTime;
    public int steps, pins;

    // in meter with decimals
    public double distance;

    // in KM/H with two decimal places
    public double avgSpeed;
    public String user;


    public List<MyLocation> myLocations = null;
    private List<LatLng> targetLocations = null;
    private List<LatLng> completedTargetLocations = null;

    public UserHistorySchema() {
    }


public UserHistorySchema(String user, Date endDateTime, Date startDateTime, List<MyLocation> myLocations, List<LatLng> targetLocations, List<LatLng> completedTargetLocations, int steps) {
        this.user = user;
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


public UserHistorySchema(String user, Date endDateTime, Date startDateTime, List<MyLocation> myLocations, List<LatLng> targetLocations, List<LatLng> completedTargetLocations, int steps, double distance, double avgSpeed, int pins) {
        this.user = user;
        this.endDateTime = endDateTime;
        this.startDateTime = startDateTime;
        this.myLocations = myLocations;
        this.targetLocations = targetLocations;
        this.completedTargetLocations = completedTargetLocations;
        this.steps = steps;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.pins = pins;
    }

    //get distance in meters between two location instance
private double getDistance() {

        double totalDistance = 0;

        for (int counter = 0; counter < myLocations.size() - 1; counter++) {

            totalDistance += myLocations.get(counter).getLocation().distanceTo(myLocations.get(counter + 1).getLocation());
        }

        return totalDistance;
    }


private double getSpeed() {
        if (endDateTime.getTime() - startDateTime.getTime() == 0) return 0;
        return Math.round((distance / (endDateTime.getTime() - startDateTime.getTime()) * 3.6) * 100.0) / 100.0;
    }

    private int getPins() {
        return completedTargetLocations.size();
    }
}
