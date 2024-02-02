package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 20;
	private static final int MATRIX_SIZE = 2000;
	private static final boolean print = false;
	private static final boolean parallel = true;
	private static final boolean sequential = true;

    public static void main(String[] args) {
		
		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] resultParallel = new double[MATRIX_SIZE][MATRIX_SIZE];
		double[][] resultSequential = new double[MATRIX_SIZE][MATRIX_SIZE];
		long startTime;
		long endTime;

		if (print) {
			System.out.println("Matrix A:");
			printMatrix(a);
			
			System.out.println("Matrix B:");
			printMatrix(b);
		}

		System.out.println("Starting matrix multiplication...");

		if(sequential){
			startTime = System.currentTimeMillis();
			resultSequential = sequentialMultiplyMatrix(a, b);
			endTime = System.currentTimeMillis();
			System.out.println("Sequential matrix multiplication took " + (endTime - startTime) + " milliseconds.");
		}
		if(parallel){
			startTime = System.currentTimeMillis();
			resultParallel = parallelMultiplyMatrix(a, b);
			endTime = System.currentTimeMillis();
			System.out.println("Parallel matrix multiplication took " + (endTime - startTime) + " milliseconds.");	
		}

		if(parallel && sequential){
			boolean matricesEqual = areMatricesEqual(resultSequential, resultParallel);
			System.out.println("Are sequential and parallel multiplication results the same? " + matricesEqual);

			if (print) {
				System.out.println("Matrix A * Matrix B (sequential):");
				printMatrix(resultSequential);
				
				System.out.println("Matrix A * Matrix B (parallel):");
				printMatrix(resultParallel);
			}
		}
	}

	/**
	 * Prints a matrix
	 * @param matrix is the matrix to be printed
	 * */

	private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%.2f ", val);
            }
            System.out.println();
        }
        System.out.println();
    }


	/**
	 * Returns the result of multiplying a row by a column
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @param i is the row of the first matrix
	 * @param j is the column of the second matrix
	 * @return the result of the multiplication
	 * */

	private static double multiplyRowByColumn(double[][] a, double[][] b, int i, int j) {
		double sum = 0;
		for (int k = 0; k < MATRIX_SIZE; k++) {
			sum += a[i][k] * b[k][j];
		}
		return sum;
	}

	/**
	 * Compares two matrices and returns true if they are equal
	 * @param matrix1 is the first matrix
	 * @param matrix2 is the second matrix
	 * @return true if the matrices are equal, false otherwise
	 * */

	private static boolean areMatricesEqual(double[][] matrix1, double[][] matrix2) {
		if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
			return false; // Matrices don't have the same dimensions
		}
		
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1[0].length; j++) {
				if (matrix1[i][j] != matrix2[i][j]) {
					return false; // Matrices differ at (i, j)
				}
			}
		}
		
		return true; // All elements are the same
	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		double[][] result = new double[a.length][b[0].length];
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				result[i][j] = multiplyRowByColumn(a, b, i, j);
			}
		}

		return result;
	}
	
	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */

	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {

	ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
	double[][] result = new double[a.length][b[0].length];

		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				Runnable task = new MultiplyMatrixTask(a, b, result, i, j);
				executor.execute(task);
			}
		}

		//wait for all tasks to finish
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		
		return result;
	}

	/**
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */

	private static double[][] generateRandomMatrix (int numRows, int numCols) {
			double matrix[][] = new double[numRows][numCols];
	for (int row = 0 ; row < numRows ; row++ ) {
		for (int col = 0 ; col < numCols ; col++ ) {
			matrix[row][col] = (double) ((int) (Math.random() * 10.0));
		}
	}
	return matrix;
    }
	
}

class MultiplyMatrixTask implements Runnable {
	private double[][] a;
	private double[][] b;
	private double result[][];
	private int i;
	private int j;


	public MultiplyMatrixTask(double[][] a, double[][] b, double[][] result, int i, int j) {
		this.a = a;
		this.b = b;
		this.result = result;
		this.i = i;
		this.j = j;
	}

	@Override
	public void run() {
		result[i][j] = multiplyRowByColumn(a, b, i, j);
	}

	private static double multiplyRowByColumn(double[][] a, double[][] b, int i, int j) {
		double sum = 0;
		for (int k = 0; k < a.length; k++) {
			sum += a[i][k] * b[k][j];
		}
		return sum;
	}
}
