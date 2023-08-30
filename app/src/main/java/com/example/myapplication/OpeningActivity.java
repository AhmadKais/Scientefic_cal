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
        Button goToLimitDerivativeButton = findViewById(R.id.gotoderivative);
        Button goToLimitIntegralButton = findViewById(R.id.gotointegral);
        Button goToUnitConverter = findViewById(R.id.gotounit);
        Button goToMatrix = findViewById(R.id.gotomatrix);



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
        goToLimitDerivativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(DerivativeActivity.class);
            }
        });

        goToLimitIntegralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(IntegralActivity.class);
            }
        });

        goToUnitConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.ConverterActivity.class);
            }
        });
        goToMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.MatrixActivity.class);
            }
        });
    }

    private void changeActivity(Class<?> destinationActivityClass) {
        Intent intent = new Intent(this, destinationActivityClass);
        startActivity(intent);
    }
}