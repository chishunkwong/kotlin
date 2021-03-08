package com.ckwong;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AverageWaitTime {

    public double averageWaitingTime(int[][] customers) {
        final AtomicInteger actualStart = new AtomicInteger(customers[0][0]);
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

    public static void main(String[] args) {
        AverageWaitTime sol = new AverageWaitTime();
        int[][] customers = {{5, 2}, {5, 4}, {10, 3}, {20, 1}};
        System.out.println(sol.averageWaitingTime(customers));
    }
}
