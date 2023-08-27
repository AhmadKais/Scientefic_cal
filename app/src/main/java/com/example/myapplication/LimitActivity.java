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
import com.github.mikephil.charting.components.XAxis;
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
    private EditText variableInput;
    private TextView resultText;
    private RadioGroup limitDirectionGroup;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);

        functionInput = findViewById(R.id.functionInput);
        pointInput = findViewById(R.id.pointInput);
        variableInput = findViewById(R.id.variableInput);
        resultText = findViewById(R.id.resultText);
        limitDirectionGroup = findViewById(R.id.limitDirectionGroup);
        chart = findViewById(R.id.chartView);

        // Configure the chart
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
    }

    public void calculateLimit(View view) {
        String function = functionInput.getText().toString();
        String pointStr = pointInput.getText().toString();
        String variable = variableInput.getText().toString();
        boolean fromLeft = ((RadioButton) findViewById(R.id.radioFromLeft)).isChecked();

        if (function.isEmpty() || pointStr.isEmpty() || variable.isEmpty()) {
            resultText.setText("Please enter a function, a point, and a variable.");
            return;
        }

        double point;
        if (pointStr.equals("∞")) {
            point = Double.POSITIVE_INFINITY;
        } else {
            point = Double.parseDouble(pointStr);
        }

        try {
            Expression expression = new ExpressionBuilder(function)
                    .variable(variable)
                    .build();

            double limitFromLeft = calculateLimitFromSide(expression, variable, point - 0.000001);
            double limitFromRight = calculateLimitFromSide(expression, variable, point + 0.000001);

            if (Math.abs(limitFromLeft - limitFromRight) < 1e-6) {
                resultText.setText("Limit from Left and Right at " + pointStr + ": " + limitFromLeft);
            } else {
                resultText.setText("Limit from Left at " + pointStr + ": " + limitFromLeft +
                        "\nLimit from Right at " + pointStr + ": " + limitFromRight);
            }

            // Create data points for the graph
            List<Entry> entries = new ArrayList<>();
            for (double x = point - 2; x <= point + 2; x += 0.1) {
                expression.setVariable(variable, x);
                float y = (float) expression.evaluate();
                entries.add(new Entry((float) x, y));
            }

            // Create a data set from the entries
            LineDataSet dataSet = new LineDataSet(entries, "Function");
            dataSet.setColor(Color.BLUE);
            dataSet.setCircleColor(Color.RED);
            dataSet.setCircleRadius(3f);
            LineData lineData = new LineData(dataSet);

            // Set the data to the chart
            chart.setData(lineData);
            chart.invalidate();
        } catch (Exception e) {
            resultText.setText("Error calculating limit.");
            chart.clear();
        }
    }

    private double calculateLimitFromSide(Expression expression, String variable, double point) {
        expression.setVariable(variable, point);
        return expression.evaluate();
    }
}
