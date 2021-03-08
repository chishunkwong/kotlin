package com.ckwong;

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
    }

    /**
     * Given the root of a BST, find the kth (1-indexed) smallest element in the tree.
     */
    public int kthSmallest(TreeNode root, int k) {
        return 0;
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
        sol.kthSmallest(root, 3);

        System.out.println("Hello Java");
    }
}
