package smartcityx.algorithms.suffix;

public class Kasai {
    public static int[] buildLCP(String txt, int[] suffixArr) {
        int n = suffixArr.length;
        int[] lcp = new int[n];
        int[] invSuff = new int[n];

        for (int i = 0; i < n; i++) {
            invSuff[suffixArr[i]] = i;
        }

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (invSuff[i] == n - 1) {
                k = 0;
                continue;
            }

            int j = suffixArr[invSuff[i] + 1];
            while (i + k < n && j + k < n && txt.charAt(i + k) == txt.charAt(j + k)) {
                k++;
            }

            lcp[invSuff[i]] = k;

            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }
}
