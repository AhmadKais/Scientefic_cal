package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OpeningActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        Button goToCalcButton = findViewById(R.id.gotocalc);
        Button goToLimitButton = findViewById(R.id.gotolimit);

        goToCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.class);
            }
        });

        goToLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(LimitActivity.class);
            }
        });
    }

    private void changeActivity(Class<?> destinationActivityClass) {
        Intent intent = new Intent(this, destinationActivityClass);
        startActivity(intent);
    }
}