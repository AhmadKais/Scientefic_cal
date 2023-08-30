package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


import java.util.ArrayList;
import java.util.List;

public class LimitActivity extends AppCompatActivity {

    private EditText functionInput;
    private EditText pointInput;
    private TextView resultText;
    private EditText variableInput;
    private RadioGroup limitDirectionGroup;
    private RadioButton radioFromLeft;
    private RadioButton radioFromRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);

        functionInput = findViewById(R.id.functionInput);
        pointInput = findViewById(R.id.pointInput);
        resultText = findViewById(R.id.resultText);
        variableInput = findViewById(R.id.variableInput);
        limitDirectionGroup = findViewById(R.id.limitDirectionGroup);
        radioFromLeft = findViewById(R.id.radioFromLeft);
        radioFromRight = findViewById(R.id.radioFromRight);
    }

    public void calculateLimit(View view) {
        String function = functionInput.getText().toString().trim();
        String pointStr = pointInput.getText().toString().trim();
        String variable = variableInput.getText().toString().trim();

        if (function.isEmpty() || variable.isEmpty() || pointInput.getText().toString().isEmpty()) {
            resultText.setText("Please enter a function, a point, and a variable.");
            clearChart();
            return;
        }

        String direction = "";
        if (radioFromLeft.isChecked() && !"inf".equalsIgnoreCase(pointStr) && !"-inf".equalsIgnoreCase(pointStr)) {
            direction = "from the left";
        } else if (radioFromRight.isChecked() && !"inf".equalsIgnoreCase(pointStr) && !"-inf".equalsIgnoreCase(pointStr)) {
            direction = "from the right";
        }

        // Check if the user entered "inf" or "-inf" for infinity
        if ("inf".equalsIgnoreCase(pointStr)) {
            pointStr = "infinity"; // We'll use the word "infinity" for the WolframAlpha API
            direction = "";  // Clear the direction if approaching infinity
        } else if ("-inf".equalsIgnoreCase(pointStr)) {
            pointStr = "-infinity";  // We'll use the word "-infinity" for the WolframAlpha API
            direction = "";  // Clear the direction if approaching negative infinity
        } else if (pointStr.isEmpty()) {
            resultText.setText("Please enter a valid point.");
            return;
        } else if (direction.isEmpty()) {
            resultText.setText("Please select the direction (From Left/From Right) or enter 'inf' or '-inf'.");
            return;
        }

        String input = "Limit of " + function + " as " + variable + " approaches " + pointStr + " " + direction;

        resultText.setText("Calculating...");

        WolframAlphaAPI.calculateLimit(input, response -> runOnUiThread(() -> resultText.setText(response)));
        updateFunctionChart(function);
    }
    private void updateFunctionChart(String function) {
        List<Entry> entries = generateFunctionDataPoints(function, -10f, 10f, 0.1f);

        LineDataSet dataSet = new LineDataSet(entries, "Function Plot");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.WHITE);

        LineData lineData = new LineData(dataSet);
        LineChart chartView = findViewById(R.id.chartView);
        chartView.setData(lineData);
        chartView.invalidate(); // refresh the chart
    }

    private List<Entry> generateFunctionDataPoints(String function, float start, float end, float step) {
        Expression expression = new ExpressionBuilder(function)
                .variable("x")
                .build();

        List<Entry> entries = new ArrayList<>();

        for (double x = start; x <= end; x += step) {
            if (Math.abs(x) < 0.0001) { // This value can be adjusted
                continue; // Skip this x value
            }
            expression.setVariable("x", x);
            double y = expression.evaluate();
            entries.add(new Entry((float) x, (float) y));
        }

        return entries;
    }


    private void clearChart() {
        LineChart chartView = findViewById(R.id.chartView);
        chartView.clear();
        chartView.invalidate();
    }

}
