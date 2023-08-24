package com.example.myapplication;
// Import statements
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class DerivativeActivity extends AppCompatActivity {

    private EditText functionInput;
    private LineChart originalChart;
    private LineChart derivativeChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derivative);

        functionInput = findViewById(R.id.functionInput);
        originalChart = findViewById(R.id.originalChart);
        derivativeChart = findViewById(R.id.derivativeChart);

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

            for (double x = -5.0; x <= 5.0; x += 0.1) {
                expression.setVariable("x", x);
                originalEntries.add(new Entry((float) x, (float) expression.evaluate()));

                double h = 0.0001; // Small increment for numerical differentiation
                double derivative = (expression.setVariable("x", x + h).evaluate() - expression.setVariable("x", x - h).evaluate()) / (2 * h);
                derivativeEntries.add(new Entry((float) x, (float) derivative));
            }

            updateChart(originalChart, originalEntries, "f(x)", Color.BLUE);
            updateChart(derivativeChart, derivativeEntries, "f'(x)", Color.GREEN);

        } catch (Exception e) {
            clearCharts();
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
}
