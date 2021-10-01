package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_map;
    private Button btn_login;
    Button btn_summary;
    private Button btn_vouchers;
    private Button btn_fitness;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_map = findViewById(R.id.btn_map);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_summary = findViewById(R.id.button_summary);
        btn_vouchers = findViewById(R.id.button_voucher);
        btn_fitness = findViewById(R.id.button_fitness);

        myApplication = (MyApplication)getApplicationContext();



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, summary.class));
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user has paused a workout session, resume it
                if (myApplication.getSessionStarted()){
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i);

                }
                // Create a brand new workout session
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to start a workout session?");

                    // Starts a workout session by clicking 'Yes'
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        myApplication.startSession();
                            Intent i = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(i);
                        }
                    });

                    // Do nothing when clicking 'No'
                    builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(myApplication, "Come back when you want to start a workout session", Toast.LENGTH_LONG).show();
                        }
                    });

                    AlertDialog alert=builder.create();
                    alert.show();
                }
            }
        });

        btn_vouchers.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
        btn_fitness.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

    }

}