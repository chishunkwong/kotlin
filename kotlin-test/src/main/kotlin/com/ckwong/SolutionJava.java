package com.ckwong;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SolutionJava {

    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        if (len < 2) return nums; // not supposed to happen
        int[] forward = new int[len];
        int[] backward = new int[len];
        forward[0] = nums[0];
        backward[len - 1] = nums[len - 1];
        for (int i = 1; i < len; i++) {
            forward[i] = nums[i] * forward[i - 1];
        }
        for (int i = len - 2; i >= 0; i--) {
            backward[i] = nums[i] * backward[i + 1];
        }
        int[] answer = new int[len];
        answer[0] = backward[1];
        answer[len - 1] = forward[len - 2];
        for (int i = 1; i < len - 1; i++) {
            answer[i] = forward[i - 1] * backward[i + 1];
        }
        return answer;
    }

    public int largestRectangleArea(int[] heights) {
        List<Integer> maximums = new LinkedList<>();
        largestRectangleArea(heights, maximums);
        int maxOfMaxs = 0;
        for (int m : maximums) {
            maxOfMaxs = Math.max(m, maxOfMaxs);
        }
        return maxOfMaxs;
    }

    /**
     * collect the area of the base rectangle, then call itself recursively
     * by chopping the histogram into smaller ones where each has bars that
     * are taller than the minimum of the passed-in histogram. Visually, it
     * means draw a horizontal line at the low water mark, then apply the same
     * logic only to the peaks that are above that horizontal line.
     * @param maximums a list of candidates that this function will add to
     */
    private void largestRectangleArea(int[] heights, List<Integer> maximums) {
        final int len = heights.length;
        if (len == 0) return;
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int h : heights) {
            min = Math.min(min, h);
            max = Math.max(max, h);
        }
        // the base rectangle
        maximums.add(min * len);
        if (min == max) {
            // we have one single rectangle
            return;
        }
        // split into smaller problems by recursively looking at only the peaks that are above the minimum
        int lastMinMatch = -1;
        for (int i = 0; i < len; i++) {
            if (heights[i] == min) {
                if (i - lastMinMatch > 1) {
                    // there are some bars above min
                    largestRectangleArea(Arrays.copyOfRange(heights, lastMinMatch + 1, i), maximums);
                }
                lastMinMatch = i;
            }
        }
        if (lastMinMatch < len - 1) {
            // the tail of the heights array that are greater than min
            largestRectangleArea(Arrays.copyOfRange(heights, lastMinMatch + 1, len), maximums);
        }
    }

    public static void main(String[] args) {
        SolutionJava sol = new SolutionJava();
        System.out.println(sol.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
        // System.out.println(sol.largestRectangleArea(new int[]{9, 0}));
        /*
        int[] answer = sol.productExceptSelf(new int[]{1, 2, 3, 4});
        for (int i : answer) {
            System.out.print(i + ",");
        }
        System.out.println("");
        */
    }
}
