package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dansdistractor.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.opencensus.tags.Tag;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int NUMBER_OF_TARGET_LOCATIONS = 5;
    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 2;
    private static final int PERMISSION_FINE_LOCATION = 10;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private MyApplication myApplication;

    // Location request is a config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;

    private LocationCallback locationCallBack;

    // Google's API for location services. The majority of the app functions using this class.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Marker currentLocationMarker = null;
    private List<Marker> targetLocationsMarker;

    private Button btn_pause;

    private GeoApiContext mGeoApiContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myApplication = (MyApplication) getApplicationContext();

        // Set all properties of LocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        // Request to access location permission from the user
        requestLocationPermission();

        btn_pause = findViewById(R.id.btn_pause);

        targetLocationsMarker = new ArrayList<>();

        // The pause/resume button
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click the button when the session is paused
                if(myApplication.getSessionPaused()){
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
                    }
                    btn_pause.setText("Pause");
                    myApplication.setSessionPaused(false);
                }
                // Click the button when the session is running
                else{
                    fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
                    btn_pause.setText("Resume");
                    myApplication.setSessionPaused(true);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateGPS(myApplication.getSessionStarted());

        // Event that is triggered whenever the update interval is met
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Toast.makeText(MapsActivity.this, "Automatically update location", Toast.LENGTH_SHORT).show();
                currentLocationMarker.remove();
                LatLng currentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLatLng);
                markerOptions.title("Lat: " + currentLatLng.latitude + "; Lon: " + currentLatLng.longitude);
                currentLocationMarker = mMap.addMarker(markerOptions);
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));

                // Check if the user has reached the target location
                checkIfReachedTargetLocation(locationResult.getLastLocation());
            }
        };

        // Set up a location update loop
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        }

        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDxdEHhWFp-mLWMc5l7xA7Ug4WTCsLVFEw")
                    .build();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // lets count the number of times that pin is clicked
                Integer clicks = (Integer) marker.getTag();
                if(clicks == null){
                    clicks = 0;
                }
                clicks++;
                marker.setTag(clicks);
                Toast.makeText(MapsActivity.this, "Marker " + marker.getTitle() + " was clicked " + marker.getTag(), Toast.LENGTH_SHORT).show();

                generateDirection(marker);

                return false;
            }
        });
    }

    private void generateDirection(Marker marker){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        DirectionsApiRequest directionsApiRequest = new DirectionsApiRequest(mGeoApiContext);

        directionsApiRequest.alternatives(false);
        directionsApiRequest.origin(new com.google.maps.model.LatLng(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude));

        directionsApiRequest.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.i("MapsActivity", "generateDirection: routes: " + result.routes[0].toString());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.i("MapsActivity", "generateDirection: Failed: " + e.toString());
            }
        });
        Log.i("MapsActivity", "generateDirection: after the request");
    }

    // Generate a list of random points
    public List<LatLng> getRandomLocation(int numOfPoints, LatLng point, int radius) {

        List<LatLng> randomPoints = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate random points
        for(int i = 0; i<numOfPoints; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
        }
        return randomPoints;
    }

    // To update the location and the UI
    private void updateGPS(Boolean sessionStarted){

        // When the user grants the location permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // User provided the permission
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
//                    currentLocation = location;
                    myApplication.getMyLocations().add(location);
                    Toast.makeText(MapsActivity.this, "Update location", Toast.LENGTH_SHORT).show();

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Lat: " + location.getLatitude() + "; Lon: " + location.getLongitude());
                    currentLocationMarker = mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                    // Run this when starting a new workout session
                    if (!sessionStarted){
                        myApplication.startSession();
                        List<LatLng> targetLocations = getRandomLocation(NUMBER_OF_TARGET_LOCATIONS, new LatLng(location.getLatitude(), location.getLongitude()), 5000);
                        myApplication.setTargetLocations(targetLocations);
                        for(LatLng targetLocation: targetLocations){
                            markerOptions = new MarkerOptions();
                            markerOptions.position(targetLocation);
                            markerOptions.title("Lat: " + targetLocation.latitude + "; Lon: " + targetLocation.longitude);
                            targetLocationsMarker.add(mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                        }
                    }

                    // Run this when the session is paused
                    if (myApplication.getSessionPaused()){
                        for(LatLng targetLocation: myApplication.getTargetLocations()){
                            markerOptions = new MarkerOptions();
                            markerOptions.position(targetLocation);
                            markerOptions.title("Lat: " + targetLocation.latitude + "; Lon: " + targetLocation.longitude);
                            mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        }
                        myApplication.setSessionPaused(false);
                        Toast.makeText(myApplication, "Resuming the workout session", Toast.LENGTH_SHORT).show();
                    }

                    myApplication.getMyLocations().add(location);
                }
            });

            // Send a toast message when it failed to update the current location
            locationTask.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MapsActivity.this, "Update location failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // When the user does not grant the location permission
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    // Check if the user has reached the target location
    private void checkIfReachedTargetLocation(Location location){
        float[] distances = new float[1];

        Iterator<Marker> itr = targetLocationsMarker.iterator();
        while(itr.hasNext()){
            Marker targetMarker = itr.next();
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), targetMarker.getPosition().latitude, targetMarker.getPosition().longitude, distances);
            Log.i("In checkIfReachedTargetLocation", "Distance: " + distances[0]);

            // Target location reached
            if(distances[0] <= 50){
                myApplication.getCompletedTargetLocations().add(targetMarker.getPosition());
                Toast.makeText(MapsActivity.this, "Target location reached", Toast.LENGTH_SHORT).show();
                targetMarker.remove();
                itr.remove();
            }
        }
    }

    // To request location permission from the user
    private void requestLocationPermission(){
        // Permission granted, do nothing
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        else{
            // Permission not granted yet
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }


    // Handle the request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS(false);
                }
                else{
                    Toast.makeText(this, "This app requires to grant location permission to be able to work", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Can safely close the map when the user has paused the workout session
        if(myApplication.getSessionPaused()){
            finish();
        }
        // Create a prompt message asking if the user wants to close the workout session
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to close this workout session?");

            // The map closes when the user press 'Yes'
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
                    myApplication.endSession();
                    finish();
                }
            });

            // The map will not be closed when the user press 'No'
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();
        }

    }
}