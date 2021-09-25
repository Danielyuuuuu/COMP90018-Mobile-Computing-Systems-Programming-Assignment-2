package com.example.dansdistractor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //    private Button button;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(view ->
        {
            Log.d("History", "History Activity 2");
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);

        });

    }
}