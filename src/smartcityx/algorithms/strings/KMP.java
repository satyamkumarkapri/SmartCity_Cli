package smartcityx.algorithms.strings;

import java.util.ArrayList;
import java.util.List;

public class KMP {

    public static List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        if (text == null || pattern == null || pattern.length() == 0) return positions;

        int[] lps = computeLPS(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == pattern.length()) {
                positions.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return positions;
    }

    private static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;
        lps[0] = 0;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
