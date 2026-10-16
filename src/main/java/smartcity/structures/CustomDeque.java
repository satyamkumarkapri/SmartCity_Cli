package smartcity.structures;

import java.util.NoSuchElementException;

/**
 * Custom double-ended queue (Deque) with circular buffer backing.
 */
public class CustomDeque<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public CustomDeque(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = DEFAULT_CAPACITY;
        this.elements = new Object[initialCapacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public CustomDeque() {
        this(DEFAULT_CAPACITY);
    }

    public void addFirst(E item) {
        if (size == elements.length) resize(elements.length * 2);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = item;
        size++;
    }

    public void addLast(E item) {
        if (size == elements.length) resize(elements.length * 2);
        elements[tail] = item;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        E item = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public E pollLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        tail = (tail - 1 + elements.length) % elements.length;
        E item = (E) elements[tail];
        elements[tail] = null;
        size--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public E peekFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return (E) elements[head];
    }

    @SuppressWarnings("unchecked")
    public E peekLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        int idx = (tail - 1 + elements.length) % elements.length;
        return (E) elements[idx];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < elements.length; i++) elements[i] = null;
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
