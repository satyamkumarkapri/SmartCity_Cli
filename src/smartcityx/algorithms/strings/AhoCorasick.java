package smartcityx.algorithms.strings;

import java.util.*;

public class AhoCorasick {
    private static final int ALPHABET_SIZE = 256;

    private static class Node {
        Node[] children = new Node[ALPHABET_SIZE];
        Node fail;
        List<String> output = new ArrayList<>();
    }

    private Node root;

    public AhoCorasick() {
        root = new Node();
    }

    public void insert(String word) {
        Node current = root;
        for (char ch : word.toCharArray()) {
            if (current.children[ch] == null) {
                current.children[ch] = new Node();
            }
            current = current.children[ch];
        }
        current.output.add(word);
    }

    public void buildFailureLinks() {
        Queue<Node> queue = new LinkedList<>();
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.add(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                if (current.children[i] != null && current.children[i] != root) {
                    Node child = current.children[i];
                    Node fail = current.fail;
                    while (fail != null && fail.children[i] == null) {
                        fail = fail.fail;
                    }
                    child.fail = (fail != null) ? fail.children[i] : root;
                    if (child.fail != null) {
                        child.output.addAll(child.fail.output);
                    }
                    queue.add(child);
                }
            }
        }
    }

    public Map<String, List<Integer>> search(String text) {
        Map<String, List<Integer>> results = new HashMap<>();
        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            while (current != null && current.children[ch] == null) {
                current = current.fail;
            }
            if (current == null) {
                current = root;
                continue;
            }
            current = current.children[ch];
            if (current == root) continue;
            
            for (String word : current.output) {
                results.putIfAbsent(word, new ArrayList<>());
                results.get(word).add(i - word.length() + 1);
            }
        }
        return results;
    }
}
