package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SplayTreeActivity extends AppCompatActivity {
    // Remove this line since SplayTreeView now owns the SplayTree instance
    // private SplayTree tree = new SplayTree();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splay_tree);

        EditText editTextValue = findViewById(R.id.editTextValue);
        Button buttonInsert = findViewById(R.id.buttonInsert);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        SplayTreeView splayTreeView = findViewById(R.id.splayTreeView);  // Initialize SplayTreeView

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int value = Integer.parseInt(editTextValue.getText().toString());
                    splayTreeView.insert(value);  // Insert using SplayTreeView's method
                } catch (NumberFormatException e) {
                    // You can handle this error or show a Toast to the user
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int value = Integer.parseInt(editTextValue.getText().toString());
                    splayTreeView.delete(value);  // Delete using SplayTreeView's method
                } catch (NumberFormatException e) {
                    // You can handle this error or show a Toast to the user
                }
            }
        });
    }
}
