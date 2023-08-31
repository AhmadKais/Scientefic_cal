package com.example.myapplication;
// Import statements

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DerivativeActivity extends AppCompatActivity {

    private EditText functionInput;
    private LineChart originalChart;
    private LineChart derivativeChart;

    private TextView derivativeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derivative);

        functionInput = findViewById(R.id.functionInput);
        originalChart = findViewById(R.id.originalChart);
        derivativeChart = findViewById(R.id.derivativeChart);
        derivativeTextView = findViewById(R.id.derivativeTextView); // Find the TextView

        // Configure the charts
        configureChart(originalChart);
        configureChart(derivativeChart);
    }

    public void calculateDerivative(View view) {
        String function = functionInput.getText().toString();

        if (function.isEmpty()) {
            clearCharts();
            return;
        }

        try {
            Expression expression = new ExpressionBuilder(function)
                    .variable("x")
                    .build();

            List<Entry> originalEntries = new ArrayList<>();
            List<Entry> derivativeEntries = new ArrayList<>();

            for (double x = -10.0; x <= 10.0; x += 0.1) {
                expression.setVariable("x", x);
                originalEntries.add(new Entry((float) x, (float) expression.evaluate()));
            }

            WolframAlphaAPI.calculateDerivative(function, response -> {
                String derivativeFunction = response; // Response from WolframAlpha should be the derivative.
                derivativeTextView.setText("f'(x) = " + derivativeFunction);

                try {
                    Expression derivativeExpression = new ExpressionBuilder(derivativeFunction)
                            .variable("x")
                            .build();

                    for (double x = -10.0; x <= 10.0; x += 0.1) {
                        derivativeExpression.setVariable("x", x);
                        derivativeEntries.add(new Entry((float) x, (float) derivativeExpression.evaluate()));
                    }

                } catch (Exception e) {
                    // If there's an issue in evaluating WolframAlpha's response, resort to the numerical method
                    for (double x = -10.0; x <= 10.0; x += 0.1) {
                        double h = 0.0001;
                        double derivative = (expression.setVariable("x", x + h).evaluate() - expression.setVariable("x", x - h).evaluate()) / (2 * h);
                        derivativeEntries.add(new Entry((float) x, (float) derivative));
                    }
                }

                updateChart(originalChart, originalEntries, "f(x)", Color.BLUE);
                updateChart(derivativeChart, derivativeEntries, "f'(x)", Color.GREEN);

            }, error -> {
                // On WolframAlpha error, resort to the numerical method
                for (double x = -10.0; x <= 10.0; x += 0.1) {
                    double h = 0.0001;
                    double derivative = (expression.setVariable("x", x + h).evaluate() - expression.setVariable("x", x - h).evaluate()) / (2 * h);
                    derivativeEntries.add(new Entry((float) x, (float) derivative));
                }

                updateChart(originalChart, originalEntries, "f(x)", Color.BLUE);
                updateChart(derivativeChart, derivativeEntries, "f'(x)", Color.GREEN);
            });

        } catch (Exception e) {
            clearCharts();
        }
    }

    private String getDerivativeFunction(String function) {
        switch (function.trim()) {
            case "x":
                return "1";
            case "sin(x)":
                return "cos(x)";
            case "cos(x)":
                return "-sin(x)";
            case "tan(x)":
                return "sec^2(x)";
            case "ln(x)":
                return "1/x";
            case "log(x)":
                return "1/(x * ln(10))";  // Assuming base 10
            case "e^x":
                return "e^x";
            case "sqrt(x)":
                return "1/(2 * sqrt(x))";
            case "x^2":
                return "2x";
            case "x^3":
                return "3x^2";
            // Add more functions as needed...
            default:
                return "Not Available";
        }
    }










    private void configureChart(LineChart chart) {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
    }

    private void updateChart(LineChart chart, List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(3f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    private void clearCharts() {
        originalChart.clear();
        derivativeChart.clear();
    }
    private double calculateAnalyticalDerivative(String function, double x) {
        switch (function.trim()) {
            case "x":
                return 1;
            case "sin(x)":
                return Math.cos(x);
            case "cos(x)":
                return -Math.sin(x);
            case "tan(x)":
                return 1 / (Math.cos(x) * Math.cos(x)); // sec^2(x)
            case "ln(x)":
                return 1 / x;
            case "log(x)":  // Assuming base 10
                return 1 / (x * Math.log(10));
            case "e^x":
                return Math.exp(x);
            case "sqrt(x)":
                return 0.5 / Math.sqrt(x);
            case "x^2":
                return 2 * x;
            case "x^3":
                return 3 * x * x;
            // Add more functions as needed...
            default:
                return Double.NaN; // Indicate that the derivative is not available
        }
    }


}
