package smartcity.structures;

import java.util.NoSuchElementException;

/**
 * Custom FIFO circular queue with automatic dynamic resizing.
 */
public class CircularQueue<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public CircularQueue(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = DEFAULT_CAPACITY;
        this.elements = new Object[initialCapacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public CircularQueue() {
        this(DEFAULT_CAPACITY);
    }

    public void enqueue(E item) {
        if (size == elements.length) {
            resize(elements.length * 2);
        }
        elements[tail] = item;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        E item = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        if (size > 0 && size == elements.length / 4 && elements.length > DEFAULT_CAPACITY) {
            resize(elements.length / 2);
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return (E) elements[head];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    private void resize(int newCapacity) {
        Object[] newArr = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr;
        head = 0;
        tail = size;
    }
}
