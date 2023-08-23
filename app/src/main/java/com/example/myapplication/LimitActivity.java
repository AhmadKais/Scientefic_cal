package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LimitActivity extends AppCompatActivity {

    private EditText functionInput;
    private EditText pointInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);

        functionInput = findViewById(R.id.functionInput);
        pointInput = findViewById(R.id.pointInput);
        resultText = findViewById(R.id.resultText);
    }

    public void calculateLimit(View view) {
        String function = functionInput.getText().toString();
        String pointStr = pointInput.getText().toString();

        if (function.isEmpty() || pointStr.isEmpty()) {
            resultText.setText("Please enter a function and a point.");
            return;
        }

        double point = Double.parseDouble(pointStr);

        try {
            Expression expression = new ExpressionBuilder(function)
                    .variable("x")
                    .build();

            expression.setVariable("x", point);
            double limit = expression.evaluate();
            resultText.setText("Limit at " + point + ": " + limit);
        } catch (Exception e) {
            resultText.setText("Error calculating limit.");
        }
    }
}
