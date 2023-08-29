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
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
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
        String function = functionInput.getText().toString().trim();
        String lowerLimitStr = lowerLimitInput.getText().toString();
        String upperLimitStr = upperLimitInput.getText().toString();

        if (function.isEmpty() || lowerLimitStr.isEmpty() || upperLimitStr.isEmpty()) {
            integralValueText.setText("Please enter a function and limits.");
            chart.clear();
            return;
        }

        try {
            Expression expression = new ExpressionBuilder(function)
                    .variable("x")
                    .build();

            double lowerLimit = Double.parseDouble(lowerLimitStr);
            double upperLimit = Double.parseDouble(upperLimitStr);

            List<Entry> integralEntries = new ArrayList<>();
            double integralValue = 0;
            double stepSize = 0.01;
            double previousY = expression.setVariable("x", lowerLimit).evaluate();
            integralValue += previousY * stepSize / 2.0;
            for (double x = lowerLimit + stepSize; x <= upperLimit; x += stepSize) {
                expression.setVariable("x", x);
                double currentY = expression.evaluate();
                integralValue += (previousY + currentY) * stepSize / 2.0;
                integralEntries.add(new Entry((float) x, (float) currentY));
                previousY = currentY;
            }

            LineDataSet dataSet = new LineDataSet(integralEntries, "Integral");
            dataSet.setColor(Color.BLUE);
            dataSet.setCircleColor(Color.RED);
            dataSet.setCircleRadius(3f);
            LineData lineData = new LineData(dataSet);

            chart.setData(lineData);
            chart.invalidate();

            String antiderivative = getAntiderivative(function);
            integralValueText.setText(String.format("Integral value: %.2f\nF(x) = %s", integralValue, antiderivative));

        } catch (Exception e) {
            integralValueText.setText("Invalid input or function.");
            chart.clear();
        }
    }

    private String getAntiderivative(String function) {
        // For now, handle the case for f(x) = x only
        if (function.equals("x")) {
            return "(x^2)/2";
        } else {
            return "Antiderivative not available for this function"; // Placeholder for other functions
        }
    }
}
