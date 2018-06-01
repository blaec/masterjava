package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final List<Future> futures = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++) {
            final int pos = i;
            futures.add(executor.submit(() -> matrixElement(pos, matrixA, matrixB, matrixC)));
        }

        for (Future future : futures) {
            future.get();
        }

        return matrixC;
    }

    private static void matrixElement(int pos, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
        final int matrixSize = matrixA.length;
        int[] columnB = new int[matrixSize];

        for (int k = 0; k < matrixSize; k++) {
            columnB[k] = matrixB[k][pos];
        }

        calcElement(pos, matrixA, matrixC, matrixSize, columnB);
    }

    private static void calcElement(int pos, int[][] matrixA, int[][] matrixC, int matrixSize, int[] columnB) {
        for (int j = 0; j < matrixSize; j++) {
            int[] columnA = matrixA[j];
            int sum = 0;
            for (int k = 0; k < matrixSize; k++) {
                sum += columnA[k] * columnB[k];
            }
            matrixC[j][pos] = sum;
        }
    }

    // optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] columnB = new int[matrixSize];
        int i = 0;
        try {

            while (true) {
                for (int k = 0; k < matrixSize; k++) {
                    columnB[k] = matrixB[k][i];
                }

                calcElement(i, matrixA, matrixC, matrixSize, columnB);
                i++;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
