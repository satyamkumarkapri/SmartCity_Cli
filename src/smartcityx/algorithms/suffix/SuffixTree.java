package smartcityx.algorithms.suffix;

import java.util.HashMap;
import java.util.Map;

public class SuffixTree {
    private class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isEndOfWord;
    }

    private Node root = new Node();

    public SuffixTree(String text) {
        for (int i = 0; i < text.length(); i++) {
            insertSuffix(text.substring(i));
        }
    }

    private void insertSuffix(String suffix) {
        Node current = root;
        for (char ch : suffix.toCharArray()) {
            current.children.putIfAbsent(ch, new Node());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
    }

    public boolean searchSubstring(String pattern) {
        Node current = root;
        for (char ch : pattern.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return false;
            }
            current = current.children.get(ch);
        }
        return true;
    }
}
