package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvlActivity extends AppCompatActivity {

    private AVLTreeView avlTreeView;
    private EditText insertValueEditText;
    private Button insertButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avl);

        avlTreeView = findViewById(R.id.avlTreeView);
        insertValueEditText = findViewById(R.id.editTextText);
        insertButton = findViewById(R.id.insert);
        deleteButton = findViewById(R.id.delete);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = insertValueEditText.getText().toString();
                if (!valueStr.isEmpty()) {
                    try {
                        int value = Integer.parseInt(valueStr);
                        avlTreeView.insert(value);
                    } catch (NumberFormatException e) {
                        Toast.makeText(AvlActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = insertValueEditText.getText().toString();
                if (!valueStr.isEmpty()) {
                    try {
                        int value = Integer.parseInt(valueStr);
                        avlTreeView.delete(value);
                    } catch (NumberFormatException e) {
                        Toast.makeText(AvlActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
