package com.ckwong;

import java.util.*;

class CitiSolutionJava {
    int solution(int[] A) {
        int ans = A[0];
        for (int i = 1; i < A.length; i++) {
            if (ans > A[i]) {
                ans = A[i];
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        CitiSolutionJava sol = new CitiSolutionJava();
        int answer = sol.solution(new int[] {-3, 1, -2, 2});
        System.out.println(answer);
    }
}