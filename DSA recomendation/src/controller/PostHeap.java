/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author subad
 */
import java.util.*;

class MaxHeapNode {
    int count;
    String column;

    public MaxHeapNode(int count, String column) {
        this.count = count;
        this.column = column;
    }
}

public class PostHeap {
    private List<MaxHeapNode> heap;

    public PostHeap(Map<String, Integer> data) {
        this.heap = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            MaxHeapNode node = new MaxHeapNode(entry.getValue(), entry.getKey());
            heap.add(node);
        }

        buildMaxHeap();
    }

    private void buildMaxHeap() {
        int heapSize = heap.size();

        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            heapify(i, heapSize);
        }
    }

    private void heapify(int index, int heapSize) {
        int largest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < heapSize && heap.get(leftChild).count > heap.get(largest).count) {
            largest = leftChild;
        }

        if (rightChild < heapSize && heap.get(rightChild).count > heap.get(largest).count) {
            largest = rightChild;
        }

        if (largest != index) {
            swap(index, largest);
            heapify(largest, heapSize);
        }
    }

    private void swap(int i, int j) {
        MaxHeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public void printMaxHeap() {
        for (MaxHeapNode node : heap) {
            System.out.println("Count: " + node.count + ", Column: " + node.column);
        }
    }
    
    public List<String> getColumns(){
        List<String> column = new ArrayList<String>();
        for (MaxHeapNode node : heap) {
            column.add(node.column);
            
        }
        return column;
    }

}

