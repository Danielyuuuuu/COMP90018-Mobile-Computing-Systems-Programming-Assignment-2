package com.example.dansdistractor;

import static java.lang.Math.toIntExact;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.dansdistractor.databaseSchema.UserHistorySchema;
import com.example.dansdistractor.utils.MyLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication singleton;

    private List<Location> myLocations = null;
    private List<LatLng> targetLocations = null;
    private List<LatLng> completedTargetLocations = null;
    private List<String> myVouchers;

    private Boolean sessionStarted = false;
    private Boolean sessionPaused = false;
    private int stepCount = 0;
    private boolean hasInitialStepCount = false;
    private int initialStepCount = 0;

    private Date startDate;
    private Date endDate;
    private Double distance;
    private Double speed;
    private int pins;
    private int goalDistance;
    private int goalSteps;

    // Access to Google Firestore
    private FirebaseFirestore db;

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

    protected Boolean getSessionPaused(){
        return sessionPaused;
    }

    protected void setSessionPaused(Boolean sessionPaused){
        this.sessionPaused = sessionPaused;
    }

    public Boolean getSessionStarted(){
        return sessionStarted;
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

    protected MyApplication getInstance(){
        return singleton;
    }

    protected void startSession(int goalDistance, int goalSteps){
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        sessionStarted = true;
        completedTargetLocations = new ArrayList<>();
        stepCount = 0;
        hasInitialStepCount = false;
        initialStepCount = 0;
        startDate = new java.util.Date();
        this.goalDistance = goalDistance;
        this.goalSteps = goalSteps;
        myVouchers = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void endSession(){
        sessionStarted = false;
        hasInitialStepCount = false;
        endDate = new java.util.Date();
        distance = getDistance();
        speed = getSpeed();
        pins = getPins();

        storeUserData();

        Intent intent = new Intent(MyApplication.this, Summary.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("goalSteps", goalSteps);
        intent.putExtra("mySteps", stepCount);
        intent.putExtra("myDistance", (int) Math.round(distance));
        intent.putExtra("myDuration", toIntExact(((endDate.getTime() - startDate.getTime())/1000/60)));
        intent.putExtra("mySpeed", speed);
        intent.putExtra("myCalorie", getCalorie());
        intent.putExtra("myPoint", pins);
        intent.putExtra("myVoucher", myVouchers.size());
        intent.putExtra("myProgress",getProgress(goalDistance,goalSteps));
        intent.putExtra("goalDistance",goalDistance);

        startActivity(intent);
    }

    public void onCreate(){
        super.onCreate();

        // Access to Google Firestore
        db = FirebaseFirestore.getInstance();

        singleton = this;
        myLocations = new ArrayList<>();
        targetLocations = new ArrayList<>();
        completedTargetLocations = new ArrayList<>();
        stepCount = 0;
        hasInitialStepCount = false;
        initialStepCount = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void storeUserData() {
        String currentFirebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ArrayList<MyLocation> myLocationsEX = new ArrayList<>();
        myLocations.forEach(l -> myLocationsEX.add(new MyLocation(l)));
        UserHistorySchema userHistory = new UserHistorySchema(currentFirebaseUserID, endDate, startDate, myLocationsEX, targetLocations, completedTargetLocations, stepCount, distance, speed, pins);

        db.collection("UserHistory")
                .add(userHistory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {

                        DocumentReference usersDocRef = db.collection("Users").document(currentFirebaseUserID);

                        usersDocRef.update("totaldistance", FieldValue.increment(distance));
                        usersDocRef.update("usertotalpins", FieldValue.increment(pins));
                        usersDocRef.update("points", FieldValue.increment(pins));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
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
        if (endDate.getTime() - startDate.getTime() == 0) return 0;
        return Math.round((distance/((endDate.getTime() - startDate.getTime())/1000) * 3.6) * 100.0)/100.0;
    }

    private int getPins(){
        return completedTargetLocations.size();
    }

    private int getProgress(Integer goalDistance, Integer goalSteps){
        if(goalDistance == 0 || goalSteps == 0) return 100;

        return (int) Math.round((distance/goalDistance + stepCount/goalSteps)*100/2);
    }

    private double getCalorie(){
        return Math.round(stepCount/1000*40*100.0)/100.0;
    }

    public List<String> getMyVouchers(){
        return myVouchers;
    }

}
