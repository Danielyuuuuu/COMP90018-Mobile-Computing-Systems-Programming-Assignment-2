package com.example.dansdistractor.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dansdistractor.R;
import com.example.dansdistractor.databaseSchema.MessageSchema;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Locator extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 1;
    public static final int DEFAULT_FAST_UPDATE_INTERVAL = 1;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final double DEFAULT_LAT_LON_DEGREES = 0.2;
    private static final int MAX_NUMBER_MESSAGE_RETURNED = 30;

    private TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    private Switch sw_locationsupdates, sw_gps;
    private Button btn_submit;
    private EditText et_content;

    // Google's API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    // variable to remember if we are tracking location or not
    boolean updateOn = false;

    // Location request is a config file for all setting related to FusedLocationProviderClient
    LocationRequest locationRequest;

    // A callback function whenever the location is updated
    LocationCallback locationCallBack;

    // Store GPS location value
    private Location userLocation;

    // Access to Google Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection("Message");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        // for leave message
        btn_submit = findViewById(R.id.btn_submit);
        et_content = findViewById(R.id.et_content);

        // set all properties of LocationRequest
        locationRequest = LocationRequest.create()
                .setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
                .setFastestInterval(1000 * DEFAULT_FAST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // event that is triggered whenever the update interval is met
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //save the location
                Location location = locationResult.getLastLocation();
                userLocation = location;
                updateUIValues();
            }
        };


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensor");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers + WIFI");
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    // turn on location tracking
                    startLocationUpdate();
                } else {
                    // turn off tracking
                    stopLocationUpdate();
                }
            }
        });

        updatedGPS();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
            }
        });

        getNearbyMessages(-38.976775, 145.3867417);

    }

    private void createMessage() {

        String[] authorNameData = {"Traveler", "Adventurer", "Sightseer", "Voyager", "Wanderer", "Explorer", "Commuter", "Peddler", "Journeyer", "Backpacker", "Straggler"};

        //equal to true when the leave message form contains error(s)
        boolean incompleteForm = false;

        String contentInput = et_content.getText().toString().trim();

        if (contentInput.isEmpty()) {
            et_content.setError("Message is required!");
            et_content.requestFocus();
            incompleteForm = true;
        }

        //terminate the register process when the form is incomplete
        if(incompleteForm) return;

        double lat = userLocation.getLatitude();
        double lon = userLocation.getLongitude();
        String  address;

        Geocoder geocoder = new Geocoder(this);

        try{
            List<Address> addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);

            address = addresses.get(0).getAddressLine(0);

        }catch (Exception e){
            address = "Unavailable";
        }


        MessageSchema message = new MessageSchema("author", lat, lon, contentInput, address);

        db.collection("Message")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Toast.makeText(Locator.this, "Message has been successfully set in the location " + lat + ", " + lon, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Locator.this, "Something when wrong please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void stopLocationUpdate() {
        tv_updates.setText("Location is not being tracked");
        tv_lat.setText("Location is not being tracked");
        tv_lon.setText("Location is not being tracked");
        tv_speed.setText("Location is not being tracked");
        tv_address.setText("Location is not being tracked");
        tv_accuracy.setText("Location is not being tracked");
        tv_altitude.setText("Location is not being tracked");
        tv_sensor.setText("Location is not being tracked");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    private void startLocationUpdate() {
        tv_updates.setText("Location is being tracked");

        // check the permissions form the user to track GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updatedGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updatedGPS();
                }else{
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void updatedGPS(){

        // get permissions form the user to track GPS
        // get the current location form the fused client
        // update the UI - i.e. set all properties in their associated text view items

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            // user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    // we got permissions Put the values of location into the UI components

                    userLocation = location;

                    updateUIValues();

                }
            });

        }else{
            // permissions not granted yet

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    // update all of the text view objects with a new location
    private void updateUIValues(){

        tv_lat.setText(String.valueOf(userLocation.getLatitude()));
        tv_lon.setText(String.valueOf(userLocation.getLongitude()));
        tv_accuracy.setText(String.valueOf(userLocation.getAccuracy()));

        if(userLocation.hasAltitude()){
            tv_altitude.setText(String.valueOf(userLocation.getAltitude()));
        }else{
            tv_altitude.setText("Unavailable");
        }

        if(userLocation.hasSpeed()){
            tv_speed.setText(String.valueOf(userLocation.getSpeed()));
        }else{
            tv_speed.setText("Unavailable");
        }

        Geocoder geocoder = new Geocoder(this);

        try{
            List<Address> addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));

        }catch (Exception e){
            tv_address.setText("Unable to get street address");
        }


    }


    public ArrayList<MessageSchema> getNearbyMessages(double lat, double lon){
        final ArrayList<MessageSchema> result = new ArrayList<MessageSchema>();
        String TAG = "database";

        messageRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                double messageLat = (double) document.getData().get("lat");
                                double messageLon = (double) document.getData().get("lon");

                                if(messageLat >= lat - DEFAULT_LAT_LON_DEGREES && messageLat <= lat + DEFAULT_LAT_LON_DEGREES && messageLon >= lon - DEFAULT_LAT_LON_DEGREES && messageLon <= lon + DEFAULT_LAT_LON_DEGREES){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    MessageSchema newMessage = new MessageSchema((String) document.getData().get("author"),
                                            lat, lon, (String) document.getData().get("content"), (String) document.getData().get("address"), (Timestamp) document.getData().get("timestamp"));
                                    result.add(newMessage);
                                }

                            }

                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        Collections.shuffle(result);
        if(result.size() < MAX_NUMBER_MESSAGE_RETURNED){
            return result;
        }

        ArrayList<MessageSchema> returnResult = (ArrayList<MessageSchema>) result.subList(0, MAX_NUMBER_MESSAGE_RETURNED);
        return returnResult;
    }

}