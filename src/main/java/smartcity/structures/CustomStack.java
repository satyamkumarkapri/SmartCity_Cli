package smartcity.structures;

import java.util.EmptyStackException;

/**
 * Custom LIFO stack built on a dynamic array.
 */
public class CustomStack<E> {
    private final DynamicArray<E> list;

    public CustomStack() {
        this.list = new DynamicArray<>();
    }

    public void push(E item) {
        list.add(item);
    }

    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return list.remove(list.size() - 1);
    }

    public E peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }
}
