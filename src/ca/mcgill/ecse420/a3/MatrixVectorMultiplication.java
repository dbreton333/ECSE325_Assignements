package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatrixVectorMultiplication {
    public static void main(String[] args) {
        int n = 10000;

        // Generate a random matrix and vector
        int[][] A = generateRandomMatrix(n, n);
        int[] B = generateRandomMatrix(n);

        // Perform the matrix-vector multiplication sequentially
        long startTime = System.currentTimeMillis();
        int[] CSequential = sequentialMatrixVectorMultiplication(A, B);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential matrix-vector multiplication took " + (endTime - startTime) + " milliseconds.");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Perform the matrix-vector multiplication in parallel
        startTime = System.currentTimeMillis();
        int[] CParallel = parallelMatrixVectorMultiplication(A, B, executor);
        endTime = System.currentTimeMillis();
        System.out.println("Parallel matrix-vector multiplication took " + (endTime - startTime) + " milliseconds.");

        // Check if the results are equal
        System.out.println("Are sequential and parallel multiplication results the same? " + vectorAreEqual(CSequential, CParallel));

        
    }

    public static int[][] generateRandomMatrix(int n, int m) {
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = (int) (Math.random() * 10);
            }
        }
        return matrix;
    }

    public static boolean vectorAreEqual(int[] a, int[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }

    public static int[] generateRandomMatrix(int n) {
        int[] matrix = new int[n];
        for (int i = 0; i < n; i++) {
            matrix[i] = (int) (Math.random() * 10);
        }
        return matrix;
    }

    public static int[] sequentialMatrixVectorMultiplication(int[][] A, int[] B) {
        int n = A.length;
        int[] C = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < B.length; j++) {
                C[i] += A[i][j] * B[j];
            }
        }
        return C;
    }

    public static int[] parallelMatrixVectorMultiplication(int[][] A, int[] B, ExecutorService executor) {
        int n = A.length;
        int[] C = new int[n];

        @SuppressWarnings("unchecked")
        Future<Integer>[] futures = new Future[n];
        
        for (int i = 0; i < n; i++){
            int row = i;

            futures[i] = executor.submit(() -> {
                int sum = 0;
                for (int j = 0; j < n; j++) {
                    sum += A[row][j] * B[j];
                }
                return sum;
            });
        }

        for (int i = 0; i < n; i++) {
            try {
                C[i] = futures[i].get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return C;
    }
}
