package com.ckwong;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

public class AverageWaitTime {

    public double averageWaitingTime(int[][] customers) {
        AtomicInteger actualStart = new AtomicInteger(customers[0][0]);

        // this line cannot be here because it will make actualStart not effectively final
        // (i.e. it has been redefined, so the compiler cannot simply make it final)
        // actualStart = new AtomicInteger(5);

        // This arr cannot be defined as the lambda just below it does not start a new scope, so the compiler would
        // complain about arr being defined already
        // int[] arr = new int[3];
        double answer = Arrays.stream(customers).mapToInt(
                arr -> {
                    // perhaps the customer walked in and no outstanding order was being made
                    actualStart.set(Math.max(arr[0], actualStart.get()));
                    int waitTime = actualStart.get() + arr[1] - arr[0];
                    actualStart.addAndGet(arr[1]);
                    return waitTime;
                }
        ).average().getAsDouble();
        return answer;
    }

    public double averageWaitingTime2(int[][] customers) {
        final AtomicInteger actualStart = new AtomicInteger(customers[0][0]);
        // Note this arr is fine, but not the lambda above
        int[] arr = new int[3];
        double answer = Arrays.stream(customers).mapToInt(
                new ToIntFunction<int[]>() {
                    @Override
                    public int applyAsInt(int[] arr) {
                        actualStart.set(Math.max(arr[0], actualStart.get()));
                        int waitTime = actualStart.get() + arr[1] - arr[0];
                        actualStart.addAndGet(arr[1]);
                        return waitTime;
                    }
                }
        ).average().getAsDouble();
        return answer;
    }

    public static void main(String[] args) {
        AverageWaitTime sol = new AverageWaitTime();
        int[][] customers = {{5, 2}, {5, 4}, {10, 3}, {20, 1}};
        System.out.println(sol.averageWaitingTime(customers));
    }
}
