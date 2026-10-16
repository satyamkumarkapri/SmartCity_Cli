package smartcity.algorithms.suffix;

import smartcity.structures.CustomHashMap;

/**
 * Suffix Automaton (Directed Acyclic Word Graph - DAWG).
 * Compactly represents all substrings of a string in O(n) states and O(n) transitions.
 * Built without java.util collections, using custom CustomHashMap.
 * Time Complexity: O(n) construction, O(m) substring search.
 * Space Complexity: O(n * sigma).
 */
public class SuffixAutomaton {
    public static class State {
        public int len;
        public int link;
        public CustomHashMap<Character, Integer> next = new CustomHashMap<>(8);

        public State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private State[] st;
    private int sz;
    private int last;
    private final String text;

    public static SuffixAutomaton build(String text) {
        return new SuffixAutomaton(text);
    }

    public boolean contains(String pattern) {
        return containsSubstring(pattern);
    }

    public SuffixAutomaton(String text) {
        this.text = text != null ? text : "";
        int maxStates = Math.max(2, this.text.length() * 2);
        st = new State[maxStates];
        st[0] = new State(0, -1);
        sz = 1;
        last = 0;

        for (int i = 0; i < this.text.length(); i++) {
            extend(this.text.charAt(i));
        }
    }

    private void extend(char c) {
        int cur = sz++;
        st[cur] = new State(st[last].len + 1, -1);
        int p = last;

        while (p != -1 && !st[p].next.containsKey(c)) {
            st[p].next.put(c, cur);
            p = st[p].link;
        }

        if (p == -1) {
            st[cur].link = 0;
        } else {
            int q = st[p].next.get(c);
            if (st[p].len + 1 == st[q].len) {
                st[cur].link = q;
            } else {
                int clone = sz++;
                st[clone] = new State(st[p].len + 1, st[q].link);
                // Copy transitions
                for (Character key : st[q].next.keys()) {
                    st[clone].next.put(key, st[q].next.get(key));
                }
                while (p != -1 && st[p].next.containsKey(c) && st[p].next.get(c) == q) {
                    st[p].next.put(c, clone);
                    p = st[p].link;
                }
                st[q].link = clone;
                st[cur].link = clone;
            }
        }
        last = cur;
    }

    /**
     * Checks if pattern is a substring of the text in O(m) time.
     */
    public boolean containsSubstring(String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;
        int p = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (!st[p].next.containsKey(c)) {
                return false;
            }
            p = st[p].next.get(c);
        }
        return true;
    }

    /**
     * Computes the exact number of distinct substrings in O(n) time.
     * Uses the property that distinct substrings = sum(st[i].len - st[st[i].link].len) for all i > 0.
     */
    public long countDistinctSubstrings() {
        long count = 0;
        for (int i = 1; i < sz; i++) {
            count += (st[i].len - st[st[i].link].len);
        }
        return count;
    }

    public int getStateCount() {
        return sz;
    }

    public String getText() {
        return text;
    }
}
