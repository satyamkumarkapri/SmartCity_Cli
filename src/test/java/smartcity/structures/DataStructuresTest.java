package smartcity.structures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Foundational Data Structures Tests")
public class DataStructuresTest {

    @Test
    @DisplayName("DynamicArray - basic operations, resizing, iteration, removal")
    public void testDynamicArray() {
        DynamicArray<String> list = new DynamicArray<>(2);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        list.add("A");
        list.add("B");
        list.add("C"); // triggers resize
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));

        list.add(1, "Inserted");
        assertEquals(4, list.size());
        assertEquals("Inserted", list.get(1));
        assertEquals("B", list.get(2));

        String removed = list.remove(1);
        assertEquals("Inserted", removed);
        assertEquals(3, list.size());

        assertTrue(list.contains("C"));
        assertFalse(list.contains("Missing"));

        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("IntArrayList - primitive operations and resizing")
    public void testIntArrayList() {
        IntArrayList arr = new IntArrayList(4);
        for (int i = 0; i < 100; i++) {
            arr.add(i * 10);
        }
        assertEquals(100, arr.size());
        assertEquals(0, arr.get(0));
        assertEquals(500, arr.get(50));
        assertEquals(990, arr.get(99));

        arr.remove(50);
        assertEquals(99, arr.size());
        assertEquals(510, arr.get(50));

        int[] copy = arr.toArray();
        assertEquals(99, copy.length);
        assertEquals(0, copy[0]);
    }

    @Test
    @DisplayName("CustomStack - LIFO operations")
    public void testCustomStack() {
        CustomStack<Integer> stack = new CustomStack<>();
        assertTrue(stack.isEmpty());
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(3, stack.size());
        assertEquals(30, stack.peek());
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("CircularQueue - FIFO operations, circular wrapping and resizing")
    public void testCircularQueue() {
        CircularQueue<Integer> q = new CircularQueue<>(3);
        assertTrue(q.isEmpty());

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(3, q.size());

        assertEquals(1, q.dequeue());
        q.enqueue(4); // Wraps circularly
        assertEquals(3, q.size());

        q.enqueue(5); // Triggers resize
        assertEquals(4, q.size());
        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    @DisplayName("CustomDeque - double ended queue operations")
    public void testCustomDeque() {
        CustomDeque<String> deque = new CustomDeque<>(4);
        deque.addFirst("Mid");
        deque.addFirst("Head");
        deque.addLast("Tail");

        assertEquals(3, deque.size());
        assertEquals("Head", deque.peekFirst());
        assertEquals("Tail", deque.peekLast());

        assertEquals("Head", deque.pollFirst());
        assertEquals("Tail", deque.pollLast());
        assertEquals("Mid", deque.pollFirst());
        assertTrue(deque.isEmpty());
    }

    @Test
    @DisplayName("CustomHashMap & CustomHashSet - collision handling and operations")
    public void testCustomHashMapAndSet() {
        CustomHashMap<String, Integer> map = new CustomHashMap<>(4);
        map.put("key1", 100);
        map.put("key2", 200);
        map.put("key3", 300);
        map.put("key1", 999); // Overwrite

        assertEquals(3, map.size());
        assertEquals(999, map.get("key1"));
        assertEquals(200, map.get("key2"));
        assertNull(map.get("keyUnknown"));

        Integer rem = map.remove("key2");
        assertEquals(200, rem);
        assertEquals(2, map.size());
        assertFalse(map.containsKey("key2"));

        CustomHashSet<Integer> set = new CustomHashSet<>();
        set.add(10);
        set.add(20);
        set.add(10); // Duplicate ignored
        assertEquals(2, set.size());
        assertTrue(set.contains(10));
        assertTrue(set.contains(20));
        assertFalse(set.contains(30));
    }

    @Test
    @DisplayName("CustomPriorityQueue - Min-Heap invariant")
    public void testCustomPriorityQueue() {
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        pq.insert(40);
        pq.insert(10);
        pq.insert(50);
        pq.insert(20);
        pq.insert(30);

        assertEquals(5, pq.size());
        assertEquals(10, pq.poll());
        assertEquals(20, pq.poll());
        assertEquals(30, pq.poll());
        assertEquals(40, pq.poll());
        assertEquals(50, pq.poll());
        assertTrue(pq.isEmpty());
    }

    @Test
    @DisplayName("DisjointSetUnion - path compression and rank union")
    public void testDisjointSetUnion() {
        DisjointSetUnion dsu = new DisjointSetUnion(10);
        assertEquals(10, dsu.getNumSets());

        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(2, 3));
        assertTrue(dsu.union(1, 3));
        assertFalse(dsu.union(0, 2)); // Already connected

        assertTrue(dsu.connected(0, 3));
        assertFalse(dsu.connected(0, 4));
        assertEquals(7, dsu.getNumSets());
    }

    @Test
    @DisplayName("CustomTrie - insertion, search, and prefix matching")
    public void testCustomTrie() {
        CustomTrie trie = new CustomTrie();
        trie.insert("algorithm");
        trie.insert("algebra");
        trie.insert("algebraic");
        trie.insert("align");

        assertTrue(trie.search("algorithm"));
        assertTrue(trie.search("algebra"));
        assertFalse(trie.search("alg")); // Prefix only, not a complete word
        assertTrue(trie.startsWith("alg"));
        assertFalse(trie.startsWith("beta"));

        DynamicArray<String> completions = trie.getWordsWithPrefix("alg", 10);
        assertEquals(3, completions.size());
    }
}
