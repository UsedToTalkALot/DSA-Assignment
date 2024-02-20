package closestNumbers;

import java.util.Arrays;

public class Closest {

    int[] tree; //given tree
    int[] sorted; //to store sorted values using inorder traversal
    int index=0;//to travel in Array:sorted

    int target;
    int num;
    int[] answer;//to store answer

    int left = 0;//left pointer
    int right;//right pointer

    Closest(int[] tree){
        this.tree = tree;
        this.right = tree.length-1;

        sorted = new int[tree.length];//sets the length of sorted tree's array
        //creates a BST for given tree
        Node root = null;
        for (int i = 0; i < tree.length; i++) {
            root = insert(root, tree[i]);
        }

        inorder(root);
    }



    public static class Node {
        int data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;

        }

    }

    void setNumber(int k) {
        this.num = k;
        answer = new int[k];

    }

    Node insert(Node root, int data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (root.data > data) {
            root.left = insert(root.left, data); // small data goes to left

        } else {
            root.right = insert(root.right, data); // greater goes to right
        }
        return root;
    }

    void inorder(Node root){//retrives sorted data and stores in sorted array
        if(root == null ){
            return;
        }
        inorder(root.left);
        System.out.print(root.data);
        sorted[index] = root.data;
        index++;
        inorder(root.right);
    }

    void setTarget(int n) {
        this.target = n;
    }

    int[] findClosest() {
        while (((right - left) + 1) > num) {
            if (target - sorted[left] > sorted[right] - target) {
                left++;
            } else {
                right--;
            }
        }
        int counter = 0;

        while (left <= right) {
            answer[counter] = sorted[left];
            counter++;
            left++;
        }

        return answer;
    }

    public static void main(String[] args) {
        int[] values = { 5, 3, 7, 9, 1 };
        Closest c = new Closest(values);

        

        c.setNumber(3);
        c.setTarget(6);

        int[] result = c.findClosest();
        System.out.println(Arrays.toString(result));
    }
}
