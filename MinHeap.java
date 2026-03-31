import java.util.ArrayList;

public class MinHeap {
    private ArrayList<Driver> heap;
    private double userX;
    private double userY;

    public MinHeap(double userX, double userY) {
        this.heap = new ArrayList<>();
        this.userX = userX;
        this.userY = userY;
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }

    private void swap(int i, int j) {
        Driver temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private double distance(Driver d) {
        return d.distanceTo(userX, userY);
    }

    public void insert(Driver driver) {
        heap.add(driver);
        heapifyUp(heap.size() - 1);
    }

    private void heapifyUp(int index) {
        while (index > 0 && distance(heap.get(index)) < distance(heap.get(parent(index)))) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    public Driver extractMin() {
    while (!heap.isEmpty()) {
        Driver root = heap.get(0);
        Driver last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        if (root.isAvailable()) {
            return root;
        }
    }
    return null;
}

    private void heapifyDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < heap.size() && distance(heap.get(left)) < distance(heap.get(smallest))) {
            smallest = left;
        }

        if (right < heap.size() && distance(heap.get(right)) < distance(heap.get(smallest))) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}