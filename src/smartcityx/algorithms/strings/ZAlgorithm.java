package smartcityx.algorithms.strings;

import java.util.ArrayList;
import java.util.List;

public class ZAlgorithm {

    public static List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        if (text == null || pattern == null || pattern.length() == 0) return positions;

        String concat = pattern + "$" + text;
        int l = concat.length();
        int[] Z = new int[l];
        getZarr(concat, Z);

        for (int i = 0; i < l; ++i) {
            if (Z[i] == pattern.length()) {
                positions.add(i - pattern.length() - 1);
            }
        }
        return positions;
    }

    private static void getZarr(String str, int[] Z) {
        int n = str.length();
        int L = 0, R = 0;

        for (int i = 1; i < n; ++i) {
            if (i > R) {
                L = R = i;
                while (R < n && str.charAt(R - L) == str.charAt(R))
                    R++;
                Z[i] = R - L;
                R--;
            } else {
                int k = i - L;
                if (Z[k] < R - i + 1)
                    Z[i] = Z[k];
                else {
                    L = i;
                    while (R < n && str.charAt(R - L) == str.charAt(R))
                        R++;
                    Z[i] = R - L;
                    R--;
                }
            }
        }
    }
}
