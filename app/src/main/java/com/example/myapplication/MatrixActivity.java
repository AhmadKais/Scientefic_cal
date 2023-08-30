package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MatrixActivity extends AppCompatActivity {

    private EditText rowsInput;
    private EditText columnsInput;
    private Spinner operationSpinner;
    private TextView resultText;

    private GridLayout matrixAGrid;
    private GridLayout matrixBGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);


        operationSpinner = findViewById(R.id.operationSpinner);
        resultText = findViewById(R.id.resultText);
        matrixAGrid = findViewById(R.id.matrixAGrid);
        matrixBGrid = findViewById(R.id.matrixBGrid);

    }

    public void onResizeMatrixAClicked(View view) {
        int rows = Integer.parseInt(((EditText) findViewById(R.id.rowsInputA)).getText().toString());
        int columns = Integer.parseInt(((EditText) findViewById(R.id.columnsInputA)).getText().toString());
        resizeMatrix(rows, columns, findViewById(R.id.matrixAGrid));
    }

    public void onResizeMatrixBClicked(View view) {
        int rows = Integer.parseInt(((EditText) findViewById(R.id.rowsInputB)).getText().toString());
        int columns = Integer.parseInt(((EditText) findViewById(R.id.columnsInputB)).getText().toString());
        resizeMatrix(rows, columns, findViewById(R.id.matrixBGrid));
    }

    private void resizeMatrix(int rows, int columns, GridLayout gridLayout) {
        gridLayout.removeAllViews();  // Clear existing views
        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                EditText editText = new EditText(this);
                editText.setHint(i + "," + j);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.rightMargin = 5;
                param.topMargin = 5;
                param.setGravity(Gravity.CENTER);
                param.rowSpec = GridLayout.spec(i);
                param.columnSpec = GridLayout.spec(j);
                editText.setLayoutParams(param);
                gridLayout.addView(editText);
            }
        }
    }


    public void calculateMatrixOperation(View view) {
        try {
            switch (operationSpinner.getSelectedItemPosition()) {
                case 0: // Assuming A*B is the first operation
                    float[][] matrixA = retrieveMatrixValues(matrixAGrid);
                    float[][] matrixB = retrieveMatrixValues(matrixBGrid);
                    float[][] multipliedMatrix = multiplyMatrices(matrixA, matrixB);
                    resultText.setText("Result Matrix:\n" + matrixToString(multipliedMatrix));
                    break;
                case 1: // Assuming inverse A is the second operation
                    float[][] originalMatrixA = retrieveMatrixValues(matrixAGrid);
                    float[][] invertedMatrixA = invertMatrix(originalMatrixA);
                    resultText.setText("Inverted Matrix A:\n" + matrixToString(invertedMatrixA));
                    break;
                case 2: // Assuming inverse B is the third operation
                    float[][] originalMatrixB = retrieveMatrixValues(matrixBGrid);
                    float[][] invertedMatrixB = invertMatrix(originalMatrixB);
                    resultText.setText("Inverted Matrix B:\n" + matrixToString(invertedMatrixB));
                    break;
            }
        } catch (MatrixOperationFailedException e) {
            resultText.setText("Matrix operation failed: " + e.getMessage());
        }
    }

    // Method to retrieve matrix values from GridLayout
    private float[][] retrieveMatrixValues(GridLayout gridLayout) {
        int rows = gridLayout.getRowCount();
        int cols = gridLayout.getColumnCount();
        float[][] matrix = new float[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                EditText editText = (EditText) gridLayout.getChildAt(i * cols + j);
                String value = editText.getText().toString();
                if (!value.isEmpty()) {
                    matrix[i][j] = Float.parseFloat(value);
                } else {
                    matrix[i][j] = 0;  // Or throw an exception if empty values are not allowed.
                }
            }
        }

        return matrix;
    }


    private float[][] invertMatrix(float[][] matrix) throws MatrixOperationFailedException {
        final int size = matrix.length;
        float[][] inverseMatrix = new float[size][size];
        MatrixUtils.inverse(matrix, inverseMatrix);
        return inverseMatrix;
    }

    private float[][] multiplyMatrices(float[][] matrixA, float[][] matrixB) throws MatrixOperationFailedException {
        int rowsA = matrixA.length;
        int columnsA = matrixA[0].length;
        int rowsB = matrixB.length;
        int columnsB = matrixB[0].length;

        if (columnsA != rowsB) {
            throw new MatrixOperationFailedException("Matrix multiplication failed: The number of columns in Matrix A must be equal to the number of rows in Matrix B.");
        }

        float[][] resultMatrix = new float[rowsA][columnsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsB; j++) {
                for (int k = 0; k < columnsA; k++) {
                    resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return resultMatrix;
    }

    private String matrixToString(float[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (float[] row : matrix) {
            for (float value : row) {
                sb.append(String.format("%.2f", value)).append("\t\t\t\t");  // Adding two tabs
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
