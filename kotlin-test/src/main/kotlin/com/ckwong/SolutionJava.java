package com.ckwong;

import java.util.Arrays;

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

    public static void main(String[] args) {
        SolutionJava sol = new SolutionJava();
        int[] answer = sol.productExceptSelf(new int[]{1, 2, 3, 4});
        for (int i : answer) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }
}
