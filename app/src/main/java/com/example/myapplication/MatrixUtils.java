package com.example.myapplication;

import java.util.Arrays;

public class MatrixUtils {
    public static void inverse(final float[][] squareMatrix, final float[][] inverseMatrix)
            throws MatrixOperationFailedException {
        final int size = squareMatrix.length;
        if (squareMatrix[0].length != size || inverseMatrix.length != size
                || inverseMatrix[0].length != size) {
            throw new MatrixOperationFailedException(
                    "--- invalid length. column should be 2 times larger than row.");
        }
        for (int i = 0; i < size; ++i) {
            Arrays.fill(inverseMatrix[i], 0.0f);
            inverseMatrix[i][i] = 1.0f;
        }
        for (int i = 0; i < size; ++i) {
            findPivotAndSwapRow(i, squareMatrix, inverseMatrix, size);
            sweep(i, squareMatrix, inverseMatrix, size);
        }
    }
    /**
     * A utility function to inverse matrix.
     * Find a pivot and swap the row of squareMatrix0 and squareMatrix1
     */
    private static void findPivotAndSwapRow(final int row,
                                            final float[][] squareMatrix0, final float[][] squareMatrix1,
                                            final int size) {
        int ip = row;
        float pivot = Math.abs(squareMatrix0[row][row]);
        for (int i = row + 1; i < size; ++i) {
            if (pivot < Math.abs(squareMatrix0[i][row])) {
                ip = i;
                pivot = Math.abs(squareMatrix0[i][row]);
            }
        }
        if (ip != row) {
            for (int j = 0; j < size; ++j) {
                final float temp0 = squareMatrix0[ip][j];
                squareMatrix0[ip][j] = squareMatrix0[row][j];
                squareMatrix0[row][j] = temp0;
                final float temp1 = squareMatrix1[ip][j];
                squareMatrix1[ip][j] = squareMatrix1[row][j];
                squareMatrix1[row][j] = temp1;
            }
        }
    }
    /**
     * A utility function to inverse matrix. This function calculates answer for each row by
     * sweeping method of Gauss Jordan elimination
     */
    private static void sweep(final int row, final float[][] squareMatrix0,
                              final float[][] squareMatrix1, final int size)
            throws MatrixOperationFailedException {
        final float pivot = squareMatrix0[row][row];
        if (pivot == 0) {
            throw new MatrixOperationFailedException(
                    "Inverse failed. Invalid pivot");
        }
        for (int j = 0; j < size; ++j) {
            squareMatrix0[row][j] /= pivot;
            squareMatrix1[row][j] /= pivot;
        }
        for (int i = 0; i < size; i++) {
            final float sweepTargetValue = squareMatrix0[i][row];
            if (i != row) {
                for (int j = row; j < size; ++j) {
                    squareMatrix0[i][j] -= sweepTargetValue
                            * squareMatrix0[row][j];
                }
                for (int j = 0; j < size; ++j) {
                    squareMatrix1[i][j] -= sweepTargetValue
                            * squareMatrix1[row][j];
                }
            }
        }
    }
}
