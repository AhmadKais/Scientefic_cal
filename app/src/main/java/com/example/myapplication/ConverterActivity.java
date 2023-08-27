package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConverterActivity extends AppCompatActivity {

    private EditText valueInput;
    private Spinner unitFrom;
    private Spinner unitTo;
    private Button convertButton;
    private TextView resultText;

    private static final String[] UNITS = {"Kilograms", "Pounds", "Miles", "Kilometers", "Celsius", "Fahrenheit"};
    private static final double KG_TO_LBS = 2.20462;
    private static final double MILES_TO_KM = 1.60934;
    private static final double C_TO_F_OFFSET = 32.0;
    private static final double C_TO_F_FACTOR = 9.0 / 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        valueInput = findViewById(R.id.valueInput);
        unitFrom = findViewById(R.id.unitFrom);
        unitTo = findViewById(R.id.unitTo);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UNITS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitFrom.setAdapter(adapter);
        unitTo.setAdapter(adapter);

        unitFrom.setSelection(0);
        unitTo.setSelection(1);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }

    private void convertUnits() {
        String selectedUnitFrom = unitFrom.getSelectedItem().toString();
        String selectedUnitTo = unitTo.getSelectedItem().toString();

        double inputValue;
        try {
            inputValue = Double.parseDouble(valueInput.getText().toString());
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input");
            return;
        }

        double result = inputValue;

        if (selectedUnitFrom.equals("Kilograms") && selectedUnitTo.equals("Pounds")) {
            result = inputValue * KG_TO_LBS;
        } else if (selectedUnitFrom.equals("Pounds") && selectedUnitTo.equals("Kilograms")) {
            result = inputValue / KG_TO_LBS;
        } else if (selectedUnitFrom.equals("Miles") && selectedUnitTo.equals("Kilometers")) {
            result = inputValue * MILES_TO_KM;
        } else if (selectedUnitFrom.equals("Kilometers") && selectedUnitTo.equals("Miles")) {
            result = inputValue / MILES_TO_KM;
        } else if (selectedUnitFrom.equals("Celsius") && selectedUnitTo.equals("Fahrenheit")) {
            result = (inputValue * C_TO_F_FACTOR) + C_TO_F_OFFSET;
        } else if (selectedUnitFrom.equals("Fahrenheit") && selectedUnitTo.equals("Celsius")) {
            result = (inputValue - C_TO_F_OFFSET) / C_TO_F_FACTOR;
        } else {
            resultText.setText("Incompatible conversion");
            return;
        }

        resultText.setText(String.valueOf(result));
    }
}
