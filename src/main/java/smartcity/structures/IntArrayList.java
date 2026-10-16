package smartcity.structures;

/**
 * Primitive int dynamic array to avoid autoboxing overhead in algorithm kernels.
 */
public class IntArrayList {
    private static final int DEFAULT_CAPACITY = 10;
    private int[] data;
    private int size;

    public IntArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.data = new int[Math.max(initialCapacity, 1)];
        this.size = 0;
    }

    public IntArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public void add(int element) {
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    public int set(int index, int element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int old = data[index];
        data[index] = element;
        return old;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int removed = data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        size--;
        return removed;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    public boolean contains(int val) {
        for (int i = 0; i < size; i++) {
            if (data[i] == val) return true;
        }
        return false;
    }

    public int[] toArray() {
        int[] result = new int[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = data.length + (data.length >> 1);
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            int[] newData = new int[newCapacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
