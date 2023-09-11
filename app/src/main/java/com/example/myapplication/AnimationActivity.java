package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Button Avlbutton = findViewById(R.id.gotoAVL);
        Button binHeap = findViewById(R.id.gotoHeap);
        Button splayButton = findViewById(R.id.gotoSplay);
        Button cartButton = findViewById(R.id.gotoCartesiantree);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.CartesianTreeActivity.class);
            }
        });

        Avlbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.AvlActivity.class);
            }
        });

        binHeap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.MinHeap.class);
            }
        });

        splayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(com.example.myapplication.SplayTreeActivity.class);
            }
        });
    }
    private void changeActivity(Class<?> destinationActivityClass) {
        Intent intent = new Intent(this, destinationActivityClass);
        startActivity(intent);
    }
}