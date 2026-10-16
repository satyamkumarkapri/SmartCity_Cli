package smartcity.structures;

/**
 * Custom Hash Table with separate chaining collision handling and collision instrumentation.
 * Does not use any java.util collections.
 */
public class CustomHashMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public static class Entry<K, V> {
        public final K key;
        public V value;
        public Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    private Entry<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int collisionCount;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CustomHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        if (loadFactor <= 0) loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
        this.collisionCount = 0;
    }

    public CustomHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public CustomHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & 0x7fffffff;
    }

    private int getIndex(K key, int capacity) {
        return hash(key) % capacity;
    }

    public V put(K key, V value) {
        if (size >= table.length * loadFactor) {
            resize(table.length * 2);
        }

        int index = getIndex(key, table.length);
        Entry<K, V> current = table[index];

        if (current == null) {
            table[index] = new Entry<>(key, value, null);
            size++;
            return null;
        }

        collisionCount++;
        while (current != null) {
            if (keysEqual(current.key, key)) {
                V oldVal = current.value;
                current.value = value;
                return oldVal;
            }
            if (current.next == null) break;
            current = current.next;
        }

        current.next = new Entry<>(key, value, null);
        size++;
        return null;
    }

    public V get(K key) {
        int index = getIndex(key, table.length);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V val = get(key);
        return val != null ? val : defaultValue;
    }

    public boolean containsKey(K key) {
        return get(key) != null || checkNullKeyPresent(key);
    }

    private boolean checkNullKeyPresent(K key) {
        int index = getIndex(key, table.length);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) return true;
            current = current.next;
        }
        return false;
    }

    public V remove(K key) {
        int index = getIndex(key, table.length);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        collisionCount = 0;
    }

    public DynamicArray<K> keys() {
        DynamicArray<K> keys = new DynamicArray<>(size);
        for (Entry<K, V> bucket : table) {
            Entry<K, V> cur = bucket;
            while (cur != null) {
                keys.add(cur.key);
                cur = cur.next;
            }
        }
        return keys;
    }

    public DynamicArray<V> values() {
        DynamicArray<V> vals = new DynamicArray<>(size);
        for (Entry<K, V> bucket : table) {
            Entry<K, V> cur = bucket;
            while (cur != null) {
                vals.add(cur.value);
                cur = cur.next;
            }
        }
        return vals;
    }

    public DynamicArray<Entry<K, V>> entries() {
        DynamicArray<Entry<K, V>> list = new DynamicArray<>(size);
        for (Entry<K, V> bucket : table) {
            Entry<K, V> cur = bucket;
            while (cur != null) {
                list.add(cur);
                cur = cur.next;
            }
        }
        return list;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        size = 0;

        for (Entry<K, V> head : oldTable) {
            Entry<K, V> cur = head;
            while (cur != null) {
                put(cur.key, cur.value);
                cur = cur.next;
            }
        }
    }

    private boolean keysEqual(K k1, K k2) {
        if (k1 == k2) return true;
        if (k1 == null || k2 == null) return false;
        return k1.equals(k2);
    }
}
