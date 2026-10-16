package smartcity.structures;

/**
 * Custom Trie (Prefix Tree) supporting word insertion, exact search,
 * prefix matching, and auto-complete suggestions.
 * Built using custom DynamicArray and node arrays (no java.util collections).
 */
public class CustomTrie {
    private static final int R = 128; // Standard ASCII/extended support

    public static class TrieNode {
        public TrieNode[] children = new TrieNode[R];
        public boolean isEndOfWord;
        public int frequency;
        public String word;
    }

    private final TrieNode root;
    private int wordCount;

    public CustomTrie() {
        this.root = new TrieNode();
        this.wordCount = 0;
    }

    public void insert(String word) {
        if (word == null || word.isEmpty()) return;
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int idx = ch < R ? ch : (ch % R);
            if (current.children[idx] == null) {
                current.children[idx] = new TrieNode();
            }
            current = current.children[idx];
        }
        if (!current.isEndOfWord) {
            current.isEndOfWord = true;
            wordCount++;
        }
        current.frequency++;
        current.word = word;
    }

    public boolean search(String word) {
        if (word == null || word.isEmpty()) return false;
        TrieNode node = getNode(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) return true;
        return getNode(prefix) != null;
    }

    public DynamicArray<String> getWordsWithPrefix(String prefix, int limit) {
        DynamicArray<String> results = new DynamicArray<>();
        if (prefix == null) return results;
        TrieNode startNode = prefix.isEmpty() ? root : getNode(prefix);
        if (startNode == null) return results;

        collectWords(startNode, results, limit);
        return results;
    }

    private void collectWords(TrieNode node, DynamicArray<String> results, int limit) {
        if (node == null || results.size() >= limit) return;
        if (node.isEndOfWord && node.word != null) {
            results.add(node.word);
        }
        for (int i = 0; i < R; i++) {
            if (node.children[i] != null) {
                collectWords(node.children[i], results, limit);
                if (results.size() >= limit) return;
            }
        }
    }

    private TrieNode getNode(String str) {
        TrieNode current = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int idx = ch < R ? ch : (ch % R);
            if (current.children[idx] == null) return null;
            current = current.children[idx];
        }
        return current;
    }

    public int getWordCount() {
        return wordCount;
    }
}
