package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MatrixActivity extends AppCompatActivity {

    private EditText matrixSizeInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        matrixSizeInput = findViewById(R.id.matrixSizeInput);
        resultText = findViewById(R.id.resultText);
    }

    public void calculateMatrixOperation(View view) {
        // Get the matrix size from the input
        int size = Integer.parseInt(matrixSizeInput.getText().toString());

        // Create your matrix using user input or generate it

        try {
            // Perform matrix operations based on user's choice
            float[][] originalMatrix = new float[size][size];  // Set this matrix
            float[][] invertedMatrix = invertMatrix(originalMatrix);

            // Update the resultText with the inverted matrix
            resultText.setText("Inverted Matrix:\n" + matrixToString(invertedMatrix));
        } catch (MatrixOperationFailedException e) {
            resultText.setText("Matrix inversion failed: " + e.getMessage());
        }
    }

    private float[][] invertMatrix(float[][] matrix) throws MatrixOperationFailedException {
        final int size = matrix.length;
        float[][] inverseMatrix = new float[size][size];
        MatrixUtils.inverse(matrix, inverseMatrix); // Using the provided invert method from MatrixUtils

        return inverseMatrix;
    }


    private String matrixToString(float[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (float[] row : matrix) {
            for (float value : row) {
                sb.append(value).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
