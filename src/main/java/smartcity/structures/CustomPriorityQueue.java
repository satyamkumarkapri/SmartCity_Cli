package smartcity.structures;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Custom Binary Heap based Priority Queue.
 * Does not use java.util.PriorityQueue.
 */
public class CustomPriorityQueue<E> {
    private final DynamicArray<E> heap;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public CustomPriorityQueue() {
        this.heap = new DynamicArray<>();
        this.comparator = (a, b) -> ((Comparable<? super E>) a).compareTo(b);
    }

    public CustomPriorityQueue(Comparator<? super E> comparator) {
        this.heap = new DynamicArray<>();
        this.comparator = comparator;
    }

    public void insert(E element) {
        if (element == null) throw new NullPointerException("Null elements not allowed in heap");
        heap.add(element);
        siftUp(heap.size() - 1);
    }

    public void add(E element) {
        insert(element);
    }

    public E peek() {
        if (isEmpty()) throw new NoSuchElementException("Heap is empty");
        return heap.get(0);
    }

    public E poll() {
        if (isEmpty()) throw new NoSuchElementException("Heap is empty");
        E top = heap.get(0);
        E last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return top;
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    private void siftUp(int index) {
        E key = heap.get(index);
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E parentVal = heap.get(parent);
            if (comparator.compare(key, parentVal) >= 0) break;
            heap.set(index, parentVal);
            index = parent;
        }
        heap.set(index, key);
    }

    private void siftDown(int index) {
        E key = heap.get(index);
        int half = heap.size() >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E childVal = heap.get(child);
            int right = child + 1;
            if (right < heap.size() && comparator.compare(childVal, heap.get(right)) > 0) {
                child = right;
                childVal = heap.get(right);
            }
            if (comparator.compare(key, childVal) <= 0) break;
            heap.set(index, childVal);
            index = child;
        }
        heap.set(index, key);
    }
}
