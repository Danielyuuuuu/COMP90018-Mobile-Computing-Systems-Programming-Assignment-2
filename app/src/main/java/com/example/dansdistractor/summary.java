package com.example.dansdistractor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class summary extends AppCompatActivity {
    private RingProgressView ringProgressView;
    int myProgress=78;
    int mySteps=210;
    double myMileage=1.2;
    int myDuration=50;
    double mySpeed=5.3;
    int myCalorie=1200;
    int myPoint=500;
    int myVoucher=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

       TextView steps = findViewById(R.id.steps);
        steps.setText(mySteps+" Steps");

        TextView mileage = findViewById(R.id.mileages);
        mileage.setText(myMileage+" KMs");

        TextView duration = findViewById(R.id.textView_duration);
        duration.setText(myDuration+" Mins");

        TextView distance = findViewById(R.id.textView_distance);
        distance.setText(myMileage+" KMs");

        TextView speed = findViewById(R.id.textView_speed);
        speed.setText(mySpeed+" KM/H");

        TextView calorie = findViewById(R.id.textView_calorie);
        calorie.setText(myCalorie+"");

        TextView point = findViewById(R.id.textView_points);
        point.setText(myPoint+"");

        TextView voucher = findViewById(R.id.textView_vouchers);
        voucher.setText(myVoucher+"");

        ringProgressView = (RingProgressView) findViewById(R.id.ringProgress);
        runChart();
    }
    private void runChart() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        int currentProgress = ringProgressView.getCurrentProgress();
                        //System.out.println(currentProgress);
//                        if (currentProgress >= 100) {
//                            currentProgress = 20;
//                        }
                        Thread.sleep(10);
                        currentProgress += 1;
                        ringProgressView.setCurrentProgress(currentProgress);
                        ringProgressView.postInvalidate();
                        //
                        if(currentProgress==myProgress){
                            break;
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}