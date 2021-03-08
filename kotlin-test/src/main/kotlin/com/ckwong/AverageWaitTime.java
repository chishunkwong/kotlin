package com.ckwong;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AverageWaitTime {

    public double averageWaitingTime(int[][] customers) {
        final AtomicInteger actualStart = new AtomicInteger(customers[0][0]);
        double answer = Arrays.stream(customers).mapToInt(
                arr -> {
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
        int[][] customers = new int[4][2];
        customers[0] = new int[]{5, 2};
        customers[1] = new int[]{5, 4};
        customers[2] = new int[]{10, 3};
        customers[3] = new int[]{20, 1};
        System.out.println(sol.averageWaitingTime(customers));
    }
}
