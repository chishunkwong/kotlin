package com.ckwong;

import java.util.HashMap;
import java.util.Map;

public class KthSmallestInBST {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }

    /**
     * Given the root of a BST, find the kth (1-indexed) smallest element in the tree.
     */
    public int kthSmallest(TreeNode root, int k) {
        return kthSmallestInternal(root, k, new HashMap<Integer, Integer[]>());
    }

    /**
     * helper function for above, with a memory of what we already counted left-right-wise
     * (I am assuming that all the nodes in the BST are unique)
     */
    private int kthSmallestInternal(TreeNode node, int k, Map<Integer, Integer[]> memory) {
        Integer[] leftRight = countLeftRight(node, memory);
        // System.out.println(leftRight[0] + ", " + leftRight[1]);
        if (k == leftRight[0] + 1) {
            return node.val;
        } else if (k <= leftRight[0]) {
            return kthSmallestInternal(node.left, k, memory);
        } else {
            // System.out.println(node);
            return kthSmallestInternal(node.right, k - 1 - leftRight[0], memory);
        }
    }

    private Integer[] countLeftRight(TreeNode node, Map<Integer, Integer[]> memory) {
        Integer[] already = memory.get(node.val);
        if (already != null) {
            System.out.println("Hit " + node.val);
            return already;
        }
        int left = 0;
        int right = 0;
        if (node.left != null) {
            Integer[] leftRight = countLeftRight(node.left, memory);
            left = 1 + leftRight[0] + leftRight[1];
        }
        if (node.right != null) {
            Integer[] leftRight = countLeftRight(node.right, memory);
            right = 1 + leftRight[0] + leftRight[1];
        }
        Integer[] answer = new Integer[]{left, right};
        memory.put(node.val, answer);
        return answer;
    }

    public static void main(String[] args) {
        KthSmallestInBST sol = new KthSmallestInBST();
        TreeNode one = new TreeNode(1);
        TreeNode two = new TreeNode(2);
        TreeNode four = new TreeNode(4);
        TreeNode three = new TreeNode(3);
        three.left = two;
        three.right = four;
        two.left = one;
        TreeNode six = new TreeNode(6);
        TreeNode root = new TreeNode(5);
        root.right = six;
        root.left = three;
        Integer answer = sol.kthSmallest(root, 4);

        System.out.println("Answer: " + answer);
    }
}
