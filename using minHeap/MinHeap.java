public class MinHeap {
    private int[] heap;
    private int size;

    public MinHeap(int capacity) {
        heap = new int[capacity];//initialize szie of the tree
        size = 0;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }

    private int rightChild(int i) {
        return 2 * i + 2;
    }

    public int getMin() {
        if (size == 0) {
            System.out.println("Heap is empty");
        }
        return heap[0];//returns the top value
    }

    public void insert(int element) {
        if (size == heap.length) {//cant add more if already full
            System.out.println("Heap is full");
        }
        heap[size] = element;
        int i = size;
        while (i > 0 && heap[parent(i)] > heap[i]) {
            swap(parent(i), i);
            i = parent(i);
        }
        size++;
    }

    public int deleteMin() {
        if (size == 0) {//nothing to delete
            System.out.println("Heap is empty");
        }
        int min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);
        return min;
    }
    public boolean isEmpty(){
        if(size==0){//if no elements in array then it's empty
            return true;
        }
        else{
            return false;

        }
    }

    private void heapify(int index) {//arrange the data where parent<child
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;
        if (left < size && heap[left] < heap[smallest]) {//update smallest if left is < smallest
            smallest = left;
        }
        if (right < size && heap[right] < heap[smallest]) {//update smallest if right is < smallest
            smallest = right;
        }
        if (smallest != index) {
            swap(index, smallest);
            heapify(smallest);
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}