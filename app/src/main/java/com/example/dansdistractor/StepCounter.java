package com.example.dansdistractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounter extends AppCompatActivity implements SensorEventListener{
    private static final int PERMISSION_ACTIVITY_RECOGNITION = 20;

    private TextView txt_stepCounter;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    private boolean isStepCounterSensorPresent;
    private int stepCount = 0;
    private boolean hasInitialStepCount = false;
    private int initialStepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        txt_stepCounter = findViewById(R.id.txt_stepCounter);

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!hasInitialStepCount){
            stepCount = 0;
            initialStepCount = (int) event.values[0];
            txt_stepCounter.setText("Step: 0");
            Toast.makeText(this, "Update step count", Toast.LENGTH_SHORT).show();
            hasInitialStepCount = true;
        }
        else{
            stepCount = (int) event.values[0] - initialStepCount;
            txt_stepCounter.setText("Step: " + stepCount);
            Toast.makeText(this, "Update step count", Toast.LENGTH_SHORT).show();
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
}