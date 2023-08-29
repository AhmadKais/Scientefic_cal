package com.example.myapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LimitActivity extends AppCompatActivity {

    private EditText functionInput;
    private EditText pointInput;
    private TextView resultText;
    private EditText variableI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);

        functionInput = findViewById(R.id.functionInput);
        pointInput = findViewById(R.id.pointInput);
        resultText = findViewById(R.id.resultText);
        variableI = findViewById(R.id.variableInput);
    }

    public void calculateLimit(View view) {
        String function = functionInput.getText().toString();
        String pointStr = pointInput.getText().toString();
        String variable = variableI.getText().toString();
        resultText.setText("Calculating");

        if (function.isEmpty() || pointStr.isEmpty() || variable.isEmpty()) {
            resultText.setText("Please enter a function, a point, and a variable.");
            return;
        }

        String input = "Limit of " + function + " as " + variable + " approaches " + pointStr;

        WolframAlphaAPI.calculateLimit(input, new WolframAlphaAPI.APIResponseListener() {
            @Override
            public void onResponse(String response) {
                // Update the UI with the response
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultText.setText(response);
                    }
                });
            }
        });
    }
}


