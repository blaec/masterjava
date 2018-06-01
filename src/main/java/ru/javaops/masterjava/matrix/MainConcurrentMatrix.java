package ru.javaops.masterjava.matrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * gkislin
 * 03.07.2016
 */
public class MainConcurrentMatrix {
    private static final int MATRIX_SIZE = 2048;
    private static final int PASS = 5;

    private final static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);


        double concurrentThreadSum = 0.;
        double concurrentStrassenThreadSum = 0.;
        int count = 1;
        while (count <= PASS) {
            System.out.println("Pass " + count);
            long start = System.currentTimeMillis();
            final int[][] concurrentMatrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
            double duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time, sec: %.3f", duration);
            concurrentThreadSum += duration;

            start = System.currentTimeMillis();
            int[][] concurrentStrassenMatrixC = new Strassen.MyRecursiveTask(matrixA, matrixB, MATRIX_SIZE).compute();
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent Strassen thread time, sec: %.3f", duration);
            concurrentStrassenThreadSum += duration;

            if (!MatrixUtil.compare(concurrentStrassenMatrixC, concurrentMatrixC)) {
                System.err.println("Comparison failed");
                executor.shutdown();
                break;
            }
            count++;
        }
        executor.shutdownNow();
        out("\nAverage concurrent thread time, sec: %.3f", concurrentThreadSum / PASS);
        out("Average concurrent Strassen thread time, sec: %.3f", concurrentStrassenThreadSum / PASS);
    }

    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }
}
