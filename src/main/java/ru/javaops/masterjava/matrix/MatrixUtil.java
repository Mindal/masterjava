package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static ru.javaops.masterjava.matrix.MainMatrix.THREAD_NUMBER;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int countOfTasksPerOneThread = matrixSize / THREAD_NUMBER;
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMBER);
        if (countOfTasksPerOneThread == 0) return singleThreadMultiply(matrixA, matrixB);
        for (int i = 0; i < matrixSize; i += countOfTasksPerOneThread) {
            int startRow = i;
            int endRow = i + countOfTasksPerOneThread;
            executor.execute(() -> {
                fillMatrixC(matrixA, matrixB, matrixC, startRow, endRow < matrixSize ? endRow : matrixSize);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        return matrixC;
    }

    private static void fillMatrixC(int[][] matrixA, int[][] matrixB, int[][] matrixC, int startRow, int endRow) {
        int matrixSize = matrixA.length;
        final int columnB[] = new int[matrixSize];
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrixSize; j++) {
                columnB[j] = matrixB[j][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * columnB[k];
                }
                matrixC[i][j] = sum;
            }
        }
    }

    // optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int columnB[] = new int[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                columnB[j] = matrixB[j][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * columnB[k];
                }
                matrixC[i][j] = sum;
            }
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
