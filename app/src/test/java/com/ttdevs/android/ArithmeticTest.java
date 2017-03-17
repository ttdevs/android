package com.ttdevs.android;

import java.util.Arrays;

/**
 * Created by ttdevs
 * 2017-03-13 (android)
 * https://github.com/ttdevs
 */
public class ArithmeticTest {
    public class BinaryTreeNode {

        public int value; // static
        public BinaryTreeNode leftNode;
        public BinaryTreeNode rightNode;
    }

    public class Solution {

        public BinaryTreeNode constructCore(int[] preorder, int[] inorder) throws Exception {
            if (preorder == null || inorder == null) {
                return null;
            }
            if (preorder.length != inorder.length) {
                throw new Exception("长度不一样，非法的输入");
            }
            BinaryTreeNode root = new BinaryTreeNode();
            for (int i = 0; i < inorder.length; i++) {
                if (inorder[i] == preorder[0]) {
                    root.value = inorder[i];
                    System.out.println(root.value);
                    root.leftNode = constructCore(Arrays.copyOfRange(preorder, 1, i + 1),
                            Arrays.copyOfRange(inorder, 0, i));
                    root.rightNode = constructCore(Arrays.copyOfRange(preorder, i + 1, preorder.length),
                            Arrays.copyOfRange(inorder, i + 1, inorder.length));
                }
            }
            return root;
        }
    }
}
