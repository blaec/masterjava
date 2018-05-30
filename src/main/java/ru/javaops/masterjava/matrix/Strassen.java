package ru.javaops.masterjava.matrix;

import static ru.javaops.masterjava.matrix.MatrixUtil.singleThreadMultiply;

public class Strassen {

    /**
     * If matrix size is below 64 - use simple matrix multiplication method
     * otherwise use Strassen algorithm
     * https://habr.com/post/313258/#comment_9874222
     *
     * @param   a     matrix a
     * @param   b     matrix b
     * @param   n     size of matrix
     */
    public static int[][] multiStrassen(int[][] a, int[][] b, int n) {
        if (n <= 64) {
            return singleThreadMultiply(a, b);
        }

        n = n >> 1;

        int[][] a11 = new int[n][n];
        int[][] a12 = new int[n][n];
        int[][] a21 = new int[n][n];
        int[][] a22 = new int[n][n];

        int[][] b11 = new int[n][n];
        int[][] b12 = new int[n][n];
        int[][] b21 = new int[n][n];
        int[][] b22 = new int[n][n];

        splitMatrix(a, a11, a12, a21, a22);
        splitMatrix(b, b11, b12, b21, b22);

        int[][] p1 = multiStrassen(summation(a11, a22), summation(b11, b22), n);
        int[][] p2 = multiStrassen(summation(a21, a22), b11, n);
        int[][] p3 = multiStrassen(a11, subtraction(b12, b22), n);
        int[][] p4 = multiStrassen(a22, subtraction(b21, b11), n);
        int[][] p5 = multiStrassen(summation(a11, a12), b22, n);
        int[][] p6 = multiStrassen(subtraction(a21, a11), summation(b11, b12), n);
        int[][] p7 = multiStrassen(subtraction(a12, a22), summation(b21, b22), n);

        int[][] c11 = summation(summation(p1, p4), subtraction(p7, p5));
        int[][] c12 = summation(p3, p5);
        int[][] c21 = summation(p2, p4);
        int[][] c22 = summation(subtraction(p1, p2), summation(p3, p6));

        return collectMatrix(c11, c12, c21, c22);
    }

    private static int[][] subtraction(int[][] a, int[][] b) {
        return mathOperation(a, b, -1);
    }

    private static int[][] summation(int[][] a, int[][] b) {
        return mathOperation(a, b, 1);
    }

    private static int[][] mathOperation(int[][] a, int[][] b, int sign) {
        int[][] c = new int[a.length][a[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                c[i][j] = a[i][j] + b[i][j] * sign;
            }
        }
        return c;
    }

    private static void splitMatrix(int[][] a, int[][] a11, int[][] a12, int[][] a21, int[][] a22) {
        int n = a.length >> 1;

        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, a11[i], 0, n);
            System.arraycopy(a[i], n, a12[i], 0, n);
            System.arraycopy(a[i + n], 0, a21[i], 0, n);
            System.arraycopy(a[i + n], n, a22[i], 0, n);
        }
    }

    private static int[][] collectMatrix(int[][] a11, int[][] a12, int[][] a21, int[][] a22) {
        int n = a11.length;
        int[][] a = new int[n << 1][n << 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(a11[i], 0, a[i], 0, n);
            System.arraycopy(a12[i], 0, a[i], n, n);
            System.arraycopy(a21[i], 0, a[i + n], 0, n);
            System.arraycopy(a22[i], 0, a[i + n], n, n);
        }
        return a;
    }
}
