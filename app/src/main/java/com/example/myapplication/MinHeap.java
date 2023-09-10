package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MinHeap extends AppCompatActivity {

    private MinHeapView minHeapView;
    private EditText insertValueEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minheap);

        minHeapView = findViewById(R.id.minHeapView);
        insertValueEditText = findViewById(R.id.insertValueEditText);
        Button insertButton = findViewById(R.id.insertButton);
        Button extractMinButton = findViewById(R.id.extractMinButton);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputValue = insertValueEditText.getText().toString();
                if(!inputValue.isEmpty()){
                    int value = Integer.parseInt(inputValue);
                    minHeapView.insert(value);
                    insertValueEditText.setText("");  // Clear the input
                }
            }
        });

        extractMinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minHeapView.extractMin();
            }
        });
    }
}
