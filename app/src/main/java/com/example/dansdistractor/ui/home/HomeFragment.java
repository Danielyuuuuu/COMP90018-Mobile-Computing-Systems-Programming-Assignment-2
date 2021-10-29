package com.example.dansdistractor.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.dansdistractor.MapsActivity;
import com.example.dansdistractor.MyApplication;
import com.example.dansdistractor.R;
import com.example.dansdistractor.databinding.FragmentHomeBinding;
import com.example.dansdistractor.utils.FetchUserData;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Bundle b = new Bundle();
    TextView textview_setting;
    Button btn_map;
    MyApplication myApplication;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btn_map = root.findViewById(R.id.btn_map);
        textview_setting = root.findViewById(R.id.setting);
        myApplication = (MyApplication)getContext().getApplicationContext();

        FragmentActivity activity = this.requireActivity();

        // Set up initial value
        b.putInt("radius", 5000);
        b.putInt("dots", 5);
        b.putInt("goalDistance", 5000);
        b.putInt("goalSteps", 6000);

        final EditText editText = new EditText(getContext());
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Settings for Workout Session");
                builder.setView(settings);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.putInt("radius", Integer.parseInt(radius.getText().toString()) * 1000);
                        b.putInt("dots", Integer.parseInt(dots.getText().toString()));
                        b.putInt("goalDistance", Integer.parseInt(goalDistance.getText().toString()));
                        b.putInt("goalSteps", Integer.parseInt(goalSteps.getText().toString()));
                        // pass goals to the charts in fitness activity
                        SharedPreferences sharedPref = activity.getSharedPreferences(FetchUserData.ALL_HISTORY, Activity.MODE_PRIVATE);
                        sharedPref
                                .edit()
                                .putInt("goalDistance", b.getInt("goalDistance"))
                                .putInt("goalSteps", b.getInt("goalSteps"))
                                .apply();
                        Toast.makeText(getContext(), "setting updated", Toast.LENGTH_LONG).show();
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
                    Intent i = new Intent(getContext(), MapsActivity.class);
                    startActivity(i);

                }
                // Create a brand new workout session
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to start a workout session?");

                    // Starts a workout session by clicking 'Yes'
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        myApplication.startSession();
                            Intent i = new Intent(getContext(), MapsActivity.class);
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}