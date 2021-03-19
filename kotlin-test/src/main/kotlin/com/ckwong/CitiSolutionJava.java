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

    public int wordMachine(String S) {
        List<Integer> numbers = new ArrayList();
        String[] parts = S.split(" ");
        for (String part : parts) {
            if (part.equals("POP")) {
                if (numbers.isEmpty()) {
                    // throw new IllegalStateException("No number to pop");
                    return -1;
                }
                numbers.remove(numbers.size() - 1);
            } else if (part.equals("DUP")){
                if (numbers.isEmpty()) {
                    // throw new IllegalStateException("No number to dup");
                    return -1;
                }
                numbers.add(numbers.get(numbers.size() - 1));
            } else if (part.equals("+")){
                int len = numbers.size();
                if (len < 2) {
                    // throw new IllegalStateException("Not two numbers left to add");
                    return -1;
                }
                int first = numbers.remove(numbers.size() - 1);
                int second = numbers.remove(numbers.size() - 1);
                if (first + second < 0) {
                    throw new IllegalStateException("overflow");
                    // return -1;
                }
                numbers.add(first + second);
            } else if (part.equals("-")){
                int len = numbers.size();
                if (len < 2) {
                    // throw new IllegalStateException("Not two numbers left to subtract");
                    return -1;
                }
                int first = numbers.remove(numbers.size() - 1);
                int second = numbers.remove(numbers.size() - 1);
                if (first < second) {
                    // throw new IllegalStateException("subtraction will result in a negative number");
                    return -1;
                }
                numbers.add(first - second);
            } else {
                // better be a non-negative integer
                int number = Integer.parseInt(part);
                if (number < 0) {
                    // throw new IllegalArgumentException("number less than 0");
                    return -1;
                }
                numbers.add(number);
            }
        }
        if (numbers.isEmpty()) {
            // throw new IllegalStateException("No numbers left");
            return -1;
        }
        return numbers.remove(numbers.size() - 1);
    }

    public static void main(String[] args) {
        CitiSolutionJava sol = new CitiSolutionJava();
        // int answer = sol.solution(new int[] {-3, 1, -2, 2});
        // System.out.println(answer);
        System.out.println(sol.wordMachine("2147483646 2147483647 +"));
        // System.out.println(sol.wordMachine("4 5 6 - 7 +"));
        // System.out.println(sol.wordMachine("13 DUP 4 POP 5 DUP + DUP + -"));
        // System.out.println(sol.wordMachine("5 6 + -"));
        // System.out.println(sol.wordMachine("3 DUP 5 - -"));
    }
}