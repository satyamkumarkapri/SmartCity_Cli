package smartcityx.algorithms.suffix;

import java.util.Arrays;
import java.util.Comparator;

public class SuffixArray {
    public static class Suffix {
        public int index;
        public String suff;

        public Suffix(int index, String suff) {
            this.index = index;
            this.suff = suff;
        }
    }

    public static int[] buildSuffixArray(String text) {
        int n = text.length();
        Suffix[] suffixes = new Suffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new Suffix(i, text.substring(i));
        }

        Arrays.sort(suffixes, Comparator.comparing(s -> s.suff));

        int[] suffixArr = new int[n];
        for (int i = 0; i < n; i++) {
            suffixArr[i] = suffixes[i].index;
        }
        return suffixArr;
    }
}
