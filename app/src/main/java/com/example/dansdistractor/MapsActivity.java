package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dansdistractor.databaseSchema.MessageSchema;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.opencensus.tags.Tag;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {
    public static int NUMBER_OF_TARGET_LOCATIONS = 5;
    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 2;
    private static final int PERMISSION_FINE_LOCATION = 10;
    private static int GENERATED_RADIUS = 5000;
    private static final double DEFAULT_LAT_LON_DEGREES = 500;
    private static final int MAX_NUMBER_MESSAGE_RETURNED = 30;

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
    private Button btn_end;
    private Button btn_leaveMessage;
    private Button btn_showMessage;

    private GeoApiContext mGeoApiContext = null;

    Polyline polyline;
    Marker polylineDestination;

    // Variables for the step counter
    private static final int PERMISSION_ACTIVITY_RECOGNITION = 20;
    private TextView txt_stepCounter;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    private boolean isStepCounterSensorPresent;
//    private int stepCount = 0;
//    private boolean hasInitialStepCount = false;
//    private int initialStepCount = 0;

    // Access to Google Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection("Message");

    ArrayList<MessageSchema> messages;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getIntent().getExtras();
        if (b != null) {
            NUMBER_OF_TARGET_LOCATIONS = b.getInt("dots");
            GENERATED_RADIUS = b.getInt("radius");
            Log.i("MapsActivity", "onCreate: b != null: dots: " + NUMBER_OF_TARGET_LOCATIONS);
            Log.i("MapsActivity", "onCreate: b != null: radius: " + GENERATED_RADIUS);
        }
        Log.i("MapsActivity", "onCreate: b == null: dots: " + NUMBER_OF_TARGET_LOCATIONS);
        Log.i("MapsActivity", "onCreate: b == null: radius: " + GENERATED_RADIUS);

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

        btn_pause = findViewById(R.id.btn_pause);
        btn_end = findViewById(R.id.btn_end);
        btn_leaveMessage = findViewById(R.id.btn_leaveMessage);
        btn_showMessage = findViewById(R.id.btn_showMessage);

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

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWorkoutPrompt();
            }
        });

        btn_leaveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExampleDialog exampleDialog = new ExampleDialog(myApplication);
                exampleDialog.show(getSupportFragmentManager(),"example dialog");
            }
        });

        btn_showMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Location> myLocations = myApplication.getMyLocations();
                if (myLocations.size() > 0){
                    Location myLocation = myLocations.get(myLocations.size() - 1);

                    // !!!!!!!!!!!!!
                    float[] distances = new float[1];

                    float shortestDistance = 0;
                    MessageSchema messageToDisplay = null;
                    Boolean hasFirst = false;
                    Iterator<MessageSchema> itr = messages.iterator();
                    while(itr.hasNext()){
                        MessageSchema messageSchema = itr.next();
                        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(), messageSchema.location.getLatitude(), messageSchema.location.getLongitude(), distances);

                        if (!hasFirst){
                            shortestDistance = distances[0];
                            messageToDisplay = messageSchema;
                            hasFirst = true;
                        }
                        else{
                            if (shortestDistance > distances[0]){
                                shortestDistance = distances[0];
                                messageToDisplay = messageSchema;
                            }
                        }
                    }

                    // !!!!!!!!!!!!!

//                    MessageSchema messageToDisplay = messages.get(0);
//
                    openShowMessageDialog("From: " + messageToDisplay.author, "Message: " + messageToDisplay.content);
//                    openShowMessageDialog("author", "content");
                }
                else{
                    Toast.makeText(MapsActivity.this, "Can't find your location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_stepCounter = findViewById(R.id.txt_steps);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        requestActivityRecognitionPermission();

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isStepCounterSensorPresent = true;
            Toast.makeText(this, "Step sensor found", Toast.LENGTH_SHORT).show();
        }
        else{
            txt_stepCounter.setText("The step counter sensor is not present");
            isStepCounterSensorPresent = false;
            Toast.makeText(this, "Step sensor not found", Toast.LENGTH_SHORT).show();
        }
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
                markerOptions.title("My current location");
                currentLocationMarker = mMap.addMarker(markerOptions);

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
                    .apiKey("AIzaSyCDjKaiU54VIeHUIjZG1eiMLBdvmB4DOH8")
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

                // Generate a direction when the user clicks onto one of the target locations
                generateDirection(marker);
                return false;
            }
        });

    }

    // To generate a direction from the current location to the selected destination
    private void generateDirection(Marker marker){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        DirectionsApiRequest directionsApiRequest = new DirectionsApiRequest(mGeoApiContext);

        directionsApiRequest.alternatives(false);
        directionsApiRequest.origin(new com.google.maps.model.LatLng(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude)).mode(TravelMode.WALKING);

        directionsApiRequest.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.i("MapsActivity", "generateDirection: routes: " + result.routes[0].toString());
                drawPolylineOnMap(result, marker);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.i("MapsActivity", "generateDirection: Failed: " + e.toString());
            }
        });
        Log.i("MapsActivity", "generateDirection: after the request");
    }

    // To draw the direction polyline on to the map
    private void drawPolylineOnMap(final DirectionsResult result, Marker marker){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DirectionsRoute route = result.routes[0];
                Log.i("MapsActivity", "drawPolylineOnMap: route: " + route.legs[0].toString());
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                // To loop through the LatLng coordinates of the polyline
                for(com.google.maps.model.LatLng latLng: decodedPath){
                    Log.i("MapsActivity", "drawPolylineOnMap: LatLng: " + latLng.toString());

                    newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                }

                // Remove the polyline that is currently in the map
                if (polyline != null){
                    polyline.remove();
                }

                // Draw the polyline only if the destination is not the current location
                if (!marker.equals(currentLocationMarker)){
                    // Draw the polyline when there is no polyline drawn before
                    if (polylineDestination == null){
                        polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                        polyline.setColor(R.color.blue);
                        polyline.setClickable(true);
                        polylineDestination = marker;
                    }
                    else{
                        // Draw the polyline only if the previous target location is not the same as the current target location
                        if(!polylineDestination.equals(marker)){
                            polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                            polyline.setColor(R.color.blue);
                            polyline.setClickable(true);
                            polylineDestination = marker;
                        }
                        // Do not draw anything, and set the polyline destination to null
                        else{
                            polylineDestination = null;
                        }
                    }
                }
                else{
                    polylineDestination = null;
                }
            }
        });
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
                    myApplication.getMyLocations().add(location);
                    Toast.makeText(MapsActivity.this, "Update location", Toast.LENGTH_SHORT).show();

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("My current location");
                    currentLocationMarker = mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                    // Run this when starting a new workout session
                    if (!sessionStarted){
                        myApplication.startSession();
                        List<LatLng> targetLocations = getRandomLocation(NUMBER_OF_TARGET_LOCATIONS, new LatLng(location.getLatitude(), location.getLongitude()), GENERATED_RADIUS);
                        myApplication.setTargetLocations(targetLocations);
                        for(LatLng targetLocation: targetLocations){
                            markerOptions = new MarkerOptions();
                            markerOptions.position(targetLocation);
                            markerOptions.title("Target location");
                            targetLocationsMarker.add(mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                        }

                        getNearbyMessages(location.getLatitude(), location.getLongitude());
                    }

                    // Run this when the session is paused
                    if (myApplication.getSessionPaused()){
                        for(LatLng targetLocation: myApplication.getTargetLocations()){
                            markerOptions = new MarkerOptions();
                            markerOptions.position(targetLocation);
                            markerOptions.title("Target location");
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

    @Override
    public void onBackPressed() {
        // Can safely close the map when the user has paused the workout session
        if(myApplication.getSessionPaused()){
            finish();
        }
        // Create a prompt message asking if the user wants to close the workout session
        else{
            closeWorkoutPrompt();
        }

    }

    private void closeWorkoutPrompt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to close this workout session?");

        // The map closes when the user press 'Yes'
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
                myApplication.endSession(b.getInt("goalDistance"), b.getInt("goalSteps"));
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!myApplication.isHasInitialStepCount()){
            myApplication.setStepCount(0);
            myApplication.setInitialStepCount((int) event.values[0]);
            txt_stepCounter.setText("Step: 0");
            myApplication.setHasInitialStepCount(true);
        }
        else{
            myApplication.setStepCount((int) event.values[0] - myApplication.getInitialStepCount());
            txt_stepCounter.setText("Step: " + myApplication.getStepCount());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            sensorManager.unregisterListener(this, mStepCounter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if(mStepCounter != null){
                sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else{
                Toast.makeText(this, "Step counter not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // To request location permission from the user
    private void requestActivityRecognitionPermission(){
        // Permission granted, do nothing
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        else{
            // Permission not granted yet
            requestPermissions(new String[] {Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_ACTIVITY_RECOGNITION);
        }
    }

    // Handle the request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_ACTIVITY_RECOGNITION:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    txt_stepCounter.setText("Step counter permission not granted");
                }
        }
    }

    private void getNearbyMessages(double lat, double lon){

        //final double DEFAULT_LAT_LON_DEGREES = 500;
        //final int MAX_NUMBER_MESSAGE_RETURNED = 30;

        //CollectionReference messageRef = db.collection("Message");

        final ArrayList<MessageSchema> result = new ArrayList<MessageSchema>();
        String TAG = "getMessage123";

        messageRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public synchronized void onComplete(@NonNull Task<QuerySnapshot> task) {
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

                            Collections.shuffle(result);
                            if(result.size() < MAX_NUMBER_MESSAGE_RETURNED){
                                Log.d(TAG, result.toString());
                                //return result;
                                displayMessages(result);
                                messages = result;
                            }else{
                                ArrayList<MessageSchema> returnResult = (ArrayList<MessageSchema>) result.subList(0, MAX_NUMBER_MESSAGE_RETURNED);
                                Log.d(TAG, returnResult.toString());
                                //return returnResult;
                                displayMessages(returnResult);
                                messages = returnResult;
                            }

//                            ArrayList<MessageSchema> returnResult = (ArrayList<MessageSchema>) result.subList(0, MAX_NUMBER_MESSAGE_RETURNED);
//                            displayMessages(returnResult);


                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Display all the nearby messages on the map
    private void displayMessages(ArrayList<MessageSchema> messages){

        Log.i("abc", "Before message");
        for(MessageSchema message: messages){
            Location messageLocation = message.location;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(messageLocation.getLatitude(), messageLocation.getLongitude()));
            markerOptions.title(messageLocation.getProvider());
            mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            Log.i("abc", "Message: " + messageLocation.toString());
        }
        Log.i("abc", "After message");

    }

    private void openShowMessageDialog(String author, String message){
        ShowMessageDialog showMessageDialog = new ShowMessageDialog().newInstance(author, message);
        showMessageDialog.show(getSupportFragmentManager(), "New Dialog");
    }
}