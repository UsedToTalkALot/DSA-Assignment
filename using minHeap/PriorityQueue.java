public class PriorityQueue {
    private MinHeap minHeap;

    public PriorityQueue(int capacity) {
        minHeap = new MinHeap(capacity);
    }

    public void enqueue(int element) {
        minHeap.insert(element);
    }

    public int dequeue() {
        return minHeap.deleteMin();
    }

    public int peek() {
        return minHeap.getMin();
    }

    public boolean isEmpty() {
        return minHeap.isEmpty();
    }

    public static void main(String[] args) {
        PriorityQueue priorityQueue = new PriorityQueue(10);

        priorityQueue.enqueue(4);
        priorityQueue.enqueue(2);
        priorityQueue.enqueue(7);

        System.out.println("Min element: " + priorityQueue.peek());

        while (!priorityQueue.isEmpty()) {
            System.out.println("Dequeued element: " + priorityQueue.dequeue());
        }
    }
}
