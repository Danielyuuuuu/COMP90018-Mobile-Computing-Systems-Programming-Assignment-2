package com.example.dansdistractor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dansdistractor.message.Locator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 2;
    private static final int PERMISSION_FINE_LOCATION = 10;

    Button btn_map;

    private Button btn_login, btn_pushup, btn_message;

    Button btn_summary;
    Button btn_leavemsg;
    Button btn_popup;
    private Button btn_vouchers;
    private Button btn_fitness;
    MyApplication myApplication;
    TextView textview_setting;
    Bundle b = new Bundle();
    private Button btn_profile;
//    public static List<String> myVouchers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_map = findViewById(R.id.btn_map);
        btn_summary = findViewById(R.id.button_summary);
        btn_leavemsg = findViewById(R.id.button_leavemsg);
        btn_vouchers = findViewById(R.id.button_voucher);
        btn_fitness = findViewById(R.id.button_fitness);
        btn_pushup = findViewById(R.id.button_pushup);
        btn_message = findViewById(R.id.button_message);
        btn_popup = findViewById(R.id.button_popup);


        textview_setting = findViewById(R.id.setting);

        myApplication = (MyApplication)getApplicationContext();
        btn_profile = findViewById(R.id.btn_profile);

        // Set up initial value
        b.putInt("radius", 5000);
        b.putInt("dots", 5);
        b.putInt("goalDistance", 5000);
        b.putInt("goalSteps", 6000);

        final EditText editText = new EditText(this);
        textview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View settings = getLayoutInflater().inflate(R.layout.dialog_workout_session_setting, null);
                EditText dots = settings.findViewById(R.id.dots);
                EditText radius = settings.findViewById(R.id.radius);
                EditText goalDistance = settings.findViewById(R.id.goalDistance);
                EditText goalSteps = settings.findViewById(R.id.goalSteps);


                dots.setText("5");
                radius.setText("5");
                goalDistance.setText("5000");
                goalSteps.setText("6000");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Settings for Workout Session");
                builder.setView(settings);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.putInt("radius", Integer.parseInt(radius.getText().toString()) * 1000);
                        b.putInt("dots", Integer.parseInt(dots.getText().toString()));
                        b.putInt("goalDistance", Integer.parseInt(goalDistance.getText().toString()));
                        b.putInt("goalSteps", Integer.parseInt(goalSteps.getText().toString()));
                        Toast.makeText(MainActivity.this, "setting updated", Toast.LENGTH_LONG).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        btn_pushup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Pushup.class));
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Locator.class));
            }
        });


        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, summary.class));
            }
        });
        btn_leavemsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
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
                            i.putExtras(b);
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

        btn_vouchers.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, VoucherActivity.class)));
        btn_fitness.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        Button button = findViewById(R.id.button_intent);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this,summary.class);
                        intent.putExtra("steps", 999);
                        intent.putExtra("myMileage", 999);
                        intent.putExtra("myDuration", 999);
                        intent.putExtra("mySpeed", 999);
                        intent.putExtra("myCalorie", 999);
                        intent.putExtra("myPoint", 999);
                        intent.putExtra("myVoucher", 999);
                        intent.putExtra("myProgress",79);
                        startActivity(intent);
                    }
                });

        requestLocationPermission();

    }
    public void openDialog() {
        MyApplication user = (MyApplication) getApplicationContext();
        ExampleDialog exampleDialog = new ExampleDialog(user);
        exampleDialog.show(getSupportFragmentManager(),"example dialog");
    }
    public void showPopup(){
        ExamplePopup examplePopup = new ExamplePopup();
        examplePopup.show(getSupportFragmentManager(),"example popup");
    }

    // Handle the request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "This app requires to grant location permission to be able to work", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else {
                        finish();
                    }
                }
                break;
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

}