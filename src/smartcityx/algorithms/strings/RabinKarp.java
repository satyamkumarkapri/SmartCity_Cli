package smartcityx.algorithms.strings;

import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    public final static int d = 256;
    public final static long q = 1000000007L;

    public static List<Integer> search(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        if (text == null || pattern == null || pattern.length() == 0 || text.length() < pattern.length()) 
            return positions;

        int M = pattern.length();
        int N = text.length();
        long p = 0; 
        long t = 0; 
        long h = 1;

        for (int i = 0; i < M - 1; i++) {
            h = (h * d) % q;
        }

        for (int i = 0; i < M; i++) {
            p = (d * p + pattern.charAt(i)) % q;
            t = (d * t + text.charAt(i)) % q;
        }

        for (int i = 0; i <= N - M; i++) {
            if (p == t) {
                boolean match = true;
                for (int j = 0; j < M; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    positions.add(i);
                }
            }
            if (i < N - M) {
                t = (d * (t - text.charAt(i) * h) + text.charAt(i + M)) % q;
                if (t < 0) {
                    t = (t + q);
                }
            }
        }
        return positions;
    }
}
