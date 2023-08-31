package com.example.myapplication;

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


public class IntegralActivity extends AppCompatActivity {

    private EditText functionInput;
    private EditText lowerLimitInput;
    private EditText upperLimitInput;
    private TextView integralValueText;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);

        functionInput = findViewById(R.id.functionInput);
        lowerLimitInput = findViewById(R.id.lowerLimitInput);
        upperLimitInput = findViewById(R.id.upperLimitInput);
        integralValueText = findViewById(R.id.integralValueText);
        chart = findViewById(R.id.chartView);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
    }

    public void calculateIntegral(View view) {
        String function = functionInput.getText().toString();

        if (function.isEmpty()) {
            integralValueText.setText("Please enter a function.");
            chart.clear();
            return;
        }

        Double lowerLimit = null;
        Double upperLimit = null;

        try {
            lowerLimit = Double.parseDouble(lowerLimitInput.getText().toString());
        } catch (NumberFormatException e) {
            // No action needed. The limit stays null.
        }

        try {
            upperLimit = Double.parseDouble(upperLimitInput.getText().toString());
        } catch (NumberFormatException e) {
            // No action needed. The limit stays null.
        }

        final double finalLowerLimit = lowerLimit != null ? lowerLimit : Double.NEGATIVE_INFINITY;
        final double finalUpperLimit = upperLimit != null ? upperLimit : Double.POSITIVE_INFINITY;

        if (lowerLimit == null && upperLimit == null) {
            // Case for antiderivative
            WolframAlphaAPI.calculateGeneralIntegral(function, result -> {
                integralValueText.setText(result);
                plotFunction(function, -10, 10);  // Default range for plotting
            }, error -> integralValueText.setText(error));
        } else if (Double.isInfinite(finalLowerLimit) || Double.isInfinite(finalUpperLimit)) {
            // Check convergence when either limit is infinity
            WolframAlphaAPI.checkConvergence(function, finalLowerLimit, finalUpperLimit, result -> {
                if (result.toLowerCase().contains("converge")) {
                    integralValueText.setText("The integral does not converge.");
                    chart.clear();
                } else {
                    integralValueText.setText(result);
                    plotFunction(function, Math.max(-10, finalLowerLimit), Math.min(10, finalUpperLimit)); // plot over a reasonable range
                }
            }, error -> integralValueText.setText(error));
        }
        else {
            // If both boundaries are finite...
            WolframAlphaAPI.calculateIntegral(function, finalLowerLimit, finalUpperLimit, result -> {
                integralValueText.setText(result);
                plotFunction(function, finalLowerLimit, finalUpperLimit);
            }, error -> integralValueText.setText(error));
        }
    }


    private double parseLimit(String limitStr) {
        if (limitStr.isEmpty()) {
            return Double.NaN;
        }

        if ("inf".equalsIgnoreCase(limitStr)) {
            return Double.POSITIVE_INFINITY;
        } else if ("-inf".equalsIgnoreCase(limitStr)) {
            return Double.NEGATIVE_INFINITY;
        } else {
            try {
                return Double.parseDouble(limitStr);
            } catch (NumberFormatException e) {
                integralValueText.setText("Invalid input for bounds.");
                chart.clear();
                return Double.NaN;
            }
        }
    }

    private void plotFunction(String function, double lowerLimit, double upperLimit) {
        try {
            Expression expression = new ExpressionBuilder(function)
                    .variable("x")
                    .build();

            List<Entry> functionEntries = new ArrayList<>();
            for (double x = lowerLimit; x <= upperLimit; x += 0.1) {
                expression.setVariable("x", x);
                double currentY = expression.evaluate();
                functionEntries.add(new Entry((float) x, (float) currentY));
            }

            LineDataSet dataSet = new LineDataSet(functionEntries, "Function");
            dataSet.setColor(Color.GREEN);
            dataSet.setCircleColor(Color.YELLOW);
            dataSet.setCircleRadius(3f);
            LineData lineData = new LineData(dataSet);

            chart.setData(lineData);
            chart.invalidate();

        } catch (Exception e) {
            integralValueText.append("\nError plotting the function.");
            chart.clear();
        }
    }


    private String getAntiderivative(String function) {
        switch (function) {
            case "x":
                return "(x^2)/2";
            case "x^2":
                return "(x^3)/3";
            case "x^3":
                return "(x^4)/4";
            case "sin(x)":
                return "-cos(x)";
            case "cos(x)":
                return "sin(x)";
            case "tan(x)":
                return "-ln(|cos(x)|)";
            case "sec^2(x)":
                return "tan(x)";
            case "e^x":
                return "e^x";
            case "ln(x)":
                return "x * ln(x) - x";
            case "1/x": // This is the natural logarithm (ln(x))
                return "ln(|x|)";
            // You can add more functions here as needed
            default:
                return "Antiderivative not available for this function";
        }
    }

}
