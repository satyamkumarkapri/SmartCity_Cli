package smartcity.algorithms.strings;

import smartcity.structures.CircularQueue;
import smartcity.structures.CustomHashMap;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;

/**
 * Aho-Corasick Multi-Pattern Exact String Search Algorithm.
 * Implemented using custom CircularQueue, DynamicArray, and CustomHashMap without java.util collections.
 * Time Complexity: O(n + sum(pattern_lengths) + matches_count).
 * Space Complexity: O(alphabet_size * sum(pattern_lengths)).
 */
public class AhoCorasick {
    private static final int ALPHABET_SIZE = 128; // Standard ASCII range

    public static class Match {
        public final String pattern;
        public final int position;

        public Match(String pattern, int position) {
            this.pattern = pattern;
            this.position = position;
        }
    }

    public static class Node {
        public Node[] children = new Node[ALPHABET_SIZE];
        public Node fail;
        public DynamicArray<String> output = new DynamicArray<>();
        public int depth = 0;
    }


    private final Node root;
    private final DynamicArray<String> patterns;

    public AhoCorasick() {
        this.root = new Node();
        this.patterns = new DynamicArray<>();
    }

    public void addPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) return;
        patterns.add(pattern);
        Node cur = root;
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            int idx = ch < ALPHABET_SIZE ? ch : (ch % ALPHABET_SIZE);
            if (cur.children[idx] == null) {
                cur.children[idx] = new Node();
                cur.children[idx].depth = cur.depth + 1;
            }
            cur = cur.children[idx];
        }
        cur.output.add(pattern);
    }

    public void buildFailureLinks() {
        CircularQueue<Node> queue = new CircularQueue<>();

        // Level 1 children fail to root
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.enqueue(root.children[i]);
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.dequeue();

            for (int i = 0; i < ALPHABET_SIZE; i++) {
                Node child = current.children[i];
                if (child != null) {
                    Node f = current.fail;
                    while (f != null && f.children[i] == null) {
                        f = f.fail;
                    }
                    child.fail = (f != null && f.children[i] != null) ? f.children[i] : root;

                    // Output link accumulation: union outputs with failure node
                    if (child.fail != null && child.fail != child) {
                        for (String pat : child.fail.output) {
                            if (!child.output.contains(pat)) {
                                child.output.add(pat);
                            }
                        }
                    }
                    queue.enqueue(child);
                }
            }
        }
    }

    public CustomHashMap<String, IntArrayList> search(String text) {
        CustomHashMap<String, IntArrayList> results = new CustomHashMap<>();
        for (String p : patterns) {
            results.put(p, new IntArrayList());
        }

        if (text == null || text.isEmpty()) return results;

        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int idx = ch < ALPHABET_SIZE ? ch : (ch % ALPHABET_SIZE);

            while (current != null && current.children[idx] == null && current != root) {
                current = current.fail;
            }

            if (current != null && current.children[idx] != null) {
                current = current.children[idx];
            } else {
                current = root;
            }

            if (current != null && !current.output.isEmpty()) {
                for (String word : current.output) {
                    IntArrayList list = results.get(word);
                    if (list == null) {
                        list = new IntArrayList();
                        results.put(word, list);
                    }
                    int startPos = i - word.length() + 1;
                    list.add(startPos);
                }
            }
        }
        return results;
    }

    public DynamicArray<Match> searchMatches(String text) {
        DynamicArray<Match> matches = new DynamicArray<>();
        CustomHashMap<String, IntArrayList> res = search(text);
        for (String pat : patterns) {
            IntArrayList positions = res.get(pat);
            if (positions != null) {
                for (int i = 0; i < positions.size(); i++) {
                    matches.add(new Match(pat, positions.get(i)));
                }
            }
        }
        return matches;
    }
}

