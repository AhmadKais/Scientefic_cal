package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CartesianTreeActivity extends AppCompatActivity {
    private CartesianTreeView cartesianTreeView;
    private EditText numberEditText;
    private Button insertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartisean_tree);

        cartesianTreeView = findViewById(R.id.cartesianTreeView);
        numberEditText = findViewById(R.id.numberEditText);
        insertButton = findViewById(R.id.insertButton);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int number = Integer.parseInt(numberEditText.getText().toString());
                    cartesianTreeView.insert(number);
                    numberEditText.setText("");  // Clear the EditText
                } catch (NumberFormatException e) {
                    // Handle the case where the user didn't enter a valid number
                }
            }
        });
    }
}
