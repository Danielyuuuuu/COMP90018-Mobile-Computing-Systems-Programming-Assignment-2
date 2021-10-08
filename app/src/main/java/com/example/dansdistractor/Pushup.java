package com.example.dansdistractor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Pushup extends AppCompatActivity implements SensorEventListener {

    private TextView message, maxrange, counter;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Boolean isProximitySensorAvailable;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushup);

        message = findViewById(R.id.message);
        maxrange = findViewById(R.id.maxrange);
        counter = findViewById(R.id.counter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isProximitySensorAvailable = true;
        }else{
            message.setText("Proximity sensor is not avaliable");
            isProximitySensorAvailable = false;
        }

        maxrange.setText(String.valueOf(proximitySensor.getMaximumRange()));


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        message.setText(event.values[0] + " cm");
        if(event.values[0] ==  proximitySensor.getMaximumRange()) return;
        count++;
        counter.setText(String.valueOf(count));




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @Override
    protected void onResume(){
        super.onResume();
        if(isProximitySensorAvailable){
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause(){
        super.onPause();

        if(isProximitySensorAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}

















