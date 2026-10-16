package smartcity.structures;

import java.util.Iterator;

/**
 * Custom Set implementation backed by CustomHashMap without using java.util.HashSet.
 */
public class CustomHashSet<E> implements Iterable<E> {
    private static final Object PRESENT = new Object();
    private final CustomHashMap<E, Object> map;

    public CustomHashSet(int initialCapacity) {
        this.map = new CustomHashMap<>(initialCapacity);
    }

    public CustomHashSet() {
        this.map = new CustomHashMap<>();
    }

    public boolean add(E element) {
        return map.put(element, PRESENT) == null;
    }

    public boolean contains(E element) {
        return map.containsKey(element);
    }

    public boolean remove(E element) {
        return map.remove(element) != null;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    public DynamicArray<E> elements() {
        return map.keys();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keys().iterator();
    }

    @Override
    public String toString() {
        return map.keys().toString();
    }
}
