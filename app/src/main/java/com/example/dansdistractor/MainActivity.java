package com.example.dansdistractor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dansdistractor.message.Locator;
import com.example.dansdistractor.vouchers.VoucherActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_map;

    private Button btn_login, btn_pushup, btn_message;
  
    Button btn_summary;
    Button btn_leavemsg;
    private Button btn_vouchers;
    private Button btn_fitness;
    MyApplication myApplication;
    TextView textview_setting;
    Bundle b = new Bundle();
    private Button btn_profile;

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



        textview_setting = findViewById(R.id.setting);

        myApplication = (MyApplication)getApplicationContext();
        btn_profile = findViewById(R.id.btn_profile);

        // Set up initial value
        b.putInt("radius", 5000);
        b.putInt("dots", 5);

        final EditText editText = new EditText(this);
        textview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View settings = getLayoutInflater().inflate(R.layout.dialog_workout_session_setting, null);
                EditText dots = settings.findViewById(R.id.dots);
                EditText radius = settings.findViewById(R.id.radius);
                dots.setText("5");
                radius.setText("5000");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Settings for Workout Session");
                builder.setView(settings);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.putInt("radius", Integer.parseInt(radius.getText().toString()));
                        b.putInt("dots", Integer.parseInt(dots.getText().toString()));
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

    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"example dialog");
    }


}