package com.example.dansdistractor.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dansdistractor.MainActivity;
import com.example.dansdistractor.MapsActivity;
import com.example.dansdistractor.MyApplication;
import com.example.dansdistractor.R;

public class HomeActivity extends AppCompatActivity {

    Bundle b = new Bundle();
    TextView textview_setting;
    Button btn_map;
    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_map = findViewById(R.id.btn_map);
        textview_setting = findViewById(R.id.setting);
        myApplication = (MyApplication)getApplicationContext();

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

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Settings for Workout Session");
                builder.setView(settings);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.putInt("radius", Integer.parseInt(radius.getText().toString()) * 1000);
                        b.putInt("dots", Integer.parseInt(dots.getText().toString()));
                        b.putInt("goalDistance", Integer.parseInt(goalDistance.getText().toString()));
                        b.putInt("goalSteps", Integer.parseInt(goalSteps.getText().toString()));
                        Toast.makeText(HomeActivity.this, "setting updated", Toast.LENGTH_LONG).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user has paused a workout session, resume it
                if (myApplication.getSessionStarted()){
                    Intent i = new Intent(HomeActivity.this, MapsActivity.class);
                    startActivity(i);

                }
                // Create a brand new workout session
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to start a workout session?");

                    // Starts a workout session by clicking 'Yes'
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        myApplication.startSession();
                            Intent i = new Intent(HomeActivity.this, MapsActivity.class);
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

    }
}