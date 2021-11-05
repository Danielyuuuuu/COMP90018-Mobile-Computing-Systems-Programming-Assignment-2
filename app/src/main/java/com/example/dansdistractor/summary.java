package com.example.dansdistractor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class summary extends AppCompatActivity {
    private RingProgressView ringProgressView;
    int myProgress=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent =getIntent();

    //set value for each item after work session done
       TextView steps = findViewById(R.id.steps);
        steps.setText(intent.getIntExtra("goalSteps",0)+" Steps");

        TextView mileage = findViewById(R.id.mileages);
        mileage.setText(intent.getIntExtra("goalDistance",0)+" Ms");

        TextView duration = findViewById(R.id.textView_duration);

        if(intent.getIntExtra("myDuration",0) == 0) duration.setText("<1 Mins");
        else duration.setText(intent.getIntExtra("myDuration",0)+" Mins");

        TextView distance = findViewById(R.id.textView_distance);
        distance.setText(intent.getIntExtra("myDistance",0)+" Ms");

        TextView speed = findViewById(R.id.textView_speed);
        speed.setText(intent.getDoubleExtra("mySpeed",0)+" KM/H");

        TextView calorie = findViewById(R.id.textView_calorie);
        calorie.setText(intent.getDoubleExtra("myCalorie",0)+"");

        TextView point = findViewById(R.id.textView_points);
        point.setText(intent.getIntExtra("mySteps",0)+"");

        TextView voucher = findViewById(R.id.textView_vouchers);
        voucher.setText(intent.getIntExtra("myVoucher",0)+"");

        myProgress=intent.getIntExtra("myProgress",0);

        Button buttonSummaryReturn = findViewById(R.id.button_summary_return);
        buttonSummaryReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary.this, NavigationActivity.class));
            }
        });
        ringProgressView = (RingProgressView) findViewById(R.id.ringProgress);
        loadChart();
    }
    //load the chart according to user's target
    private void loadChart() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        int currentProgress = ringProgressView.getCurrentProgress();
                        if(currentProgress==myProgress){
                            break;
                        }
                        Thread.sleep(10);
                        currentProgress += 1;
                        ringProgressView.setCurrentProgress(currentProgress);
                        ringProgressView.postInvalidate();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}