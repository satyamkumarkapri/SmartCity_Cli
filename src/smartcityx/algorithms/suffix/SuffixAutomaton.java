package smartcityx.algorithms.suffix;

import java.util.HashMap;
import java.util.Map;

public class SuffixAutomaton {
    private static class State {
        int len;
        int link;
        Map<Character, Integer> next = new HashMap<>();

        State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private State[] st;
    private int sz;
    private int last;

    public SuffixAutomaton(String text) {
        st = new State[text.length() * 2];
        st[0] = new State(0, -1);
        sz = 1;
        last = 0;
        for (char c : text.toCharArray()) {
            extend(c);
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
                st[clone].next.putAll(st[q].next);
                while (p != -1 && st[p].next.getOrDefault(c, -1) == q) {
                    st[p].next.put(c, clone);
                    p = st[p].link;
                }
                st[q].link = st[cur].link = clone;
            }
        }
        last = cur;
    }

    public boolean searchSubstring(String pattern) {
        int p = 0;
        for (char c : pattern.toCharArray()) {
            if (!st[p].next.containsKey(c)) {
                return false;
            }
            p = st[p].next.get(c);
        }
        return true;
    }
}
