package smartcity.algorithms.suffix;

import smartcity.structures.CustomSort;
import smartcity.structures.IntArrayList;

/**
 * Suffix Array construction using Prefix Doubling O(n log^2 n)
 * with binary search substring matching.
 * Does not use java.util collections or Arrays.sort.
 */
public class SuffixArray {

    public static class SuffixRank {
        public int index;
        public int rank1;
        public int rank2;

        public SuffixRank(int index, int rank1, int rank2) {
            this.index = index;
            this.rank1 = rank1;
            this.rank2 = rank2;
        }
    }

    /**
     * Builds the Suffix Array in linear O(N) time and O(N) auxiliary space
     * using the Suffix Array Induced Sorting (SA-IS) algorithm (Nong, Zhang, Chan).
     * Implements requirement: "SA-IS for large-scale city-document indexing."
     */
    public static int[] buildSAIS(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();
        if (n == 1) return new int[]{0};

        int[] s = new int[n + 1];
        int maxChar = 0;
        for (int i = 0; i < n; i++) {
            s[i] = (int) text.charAt(i) + 1;
            if (s[i] > maxChar) maxChar = s[i];
        }
        s[n] = 0; // Sentinel 0 < all characters

        int[] fullSA = sais(s, n + 1, maxChar + 1);
        int[] result = new int[n];
        System.arraycopy(fullSA, 1, result, 0, n);
        return result;
    }

    private static int[] sais(int[] s, int n, int K) {
        if (n <= 0) return new int[0];
        if (n == 1) return new int[]{0};

        // 1. Classify S-type and L-type
        boolean[] isS = new boolean[n];
        isS[n - 1] = true;
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] < s[i + 1]) {
                isS[i] = true;
            } else if (s[i] > s[i + 1]) {
                isS[i] = false;
            } else {
                isS[i] = isS[i + 1];
            }
        }

        // Count LMS characters
        int lmsCount = 0;
        for (int i = 1; i < n; i++) {
            if (isS[i] && !isS[i - 1]) lmsCount++;
        }
        int[] lms = new int[lmsCount];
        int ptr = 0;
        for (int i = 1; i < n; i++) {
            if (isS[i] && !isS[i - 1]) {
                lms[ptr++] = i;
            }
        }

        // Bucket boundaries
        int[] count = new int[K];
        for (int i = 0; i < n; i++) count[s[i]]++;
        int[] bktStart = new int[K];
        int[] bktEnd = new int[K];
        int sum = 0;
        for (int c = 0; c < K; c++) {
            bktStart[c] = sum;
            sum += count[c];
            bktEnd[c] = sum;
        }

        // Initial induced sort with LMS characters
        int[] sa = inducedSort(s, n, K, isS, bktStart, bktEnd, lms);

        // Extract ordered LMS positions
        int[] orderedLMS = new int[lmsCount];
        int ordPtr = 0;
        for (int i = 0; i < n; i++) {
            int pos = sa[i];
            if (pos > 0 && isS[pos] && !isS[pos - 1]) {
                orderedLMS[ordPtr++] = pos;
            }
        }

        // Name LMS substrings
        int name = 0;
        int[] names = new int[n];
        for (int i = 0; i < n; i++) names[i] = -1;
        names[orderedLMS[0]] = 0;
        int prev = orderedLMS[0];

        for (int idx = 1; idx < lmsCount; idx++) {
            int cur = orderedLMS[idx];
            boolean diff = false;
            for (int d = 0; d < n; d++) {
                int pVal = s[prev + d];
                int cVal = s[cur + d];
                boolean pLms = (prev + d > 0 && isS[prev + d] && !isS[prev + d - 1]);
                boolean cLms = (cur + d > 0 && isS[cur + d] && !isS[cur + d - 1]);
                if (pVal != cVal || pLms != cLms) {
                    diff = true;
                    break;
                }
                if (d > 0 && (pLms || cLms)) {
                    break;
                }
            }
            if (diff) name++;
            names[cur] = name;
            prev = cur;
        }

        int[] reducedS = new int[lmsCount];
        for (int i = 0; i < lmsCount; i++) {
            reducedS[i] = names[lms[i]];
        }

        int[] finalLMSOrder;
        if (name + 1 < lmsCount) {
            int[] reducedSA = sais(reducedS, lmsCount, name + 1);
            finalLMSOrder = new int[lmsCount];
            for (int i = 0; i < lmsCount; i++) {
                finalLMSOrder[i] = lms[reducedSA[i]];
            }
        } else {
            finalLMSOrder = new int[lmsCount];
            for (int i = 0; i < lmsCount; i++) {
                finalLMSOrder[names[lms[i]]] = lms[i];
            }
        }

        return inducedSort(s, n, K, isS, bktStart, bktEnd, finalLMSOrder);
    }

    private static int[] inducedSort(int[] s, int n, int K, boolean[] isS,
                                     int[] bktStart, int[] bktEnd, int[] lmsOrder) {
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) sa[i] = -1;

        int[] tail = new int[K];
        System.arraycopy(bktEnd, 0, tail, 0, K);
        for (int i = lmsOrder.length - 1; i >= 0; i--) {
            int pos = lmsOrder[i];
            int c = s[pos];
            sa[--tail[c]] = pos;
        }

        int[] head = new int[K];
        System.arraycopy(bktStart, 0, head, 0, K);
        for (int i = 0; i < n; i++) {
            int j = sa[i] - 1;
            if (j >= 0 && !isS[j]) {
                int c = s[j];
                sa[head[c]++] = j;
            }
        }

        System.arraycopy(bktEnd, 0, tail, 0, K);
        for (int i = n - 1; i >= 0; i--) {
            int j = sa[i] - 1;
            if (j >= 0 && isS[j]) {
                int c = s[j];
                sa[--tail[c]] = j;
            }
        }

        return sa;
    }

    /**
     * Builds the Suffix Array for a given text using O(n log^2 n) prefix doubling.
     */
    public static int[] buildSuffixArray(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();

        SuffixRank[] suffixes = new SuffixRank[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank(i, text.charAt(i), (i + 1 < n) ? text.charAt(i + 1) : -1);
        }

        CustomSort.mergeSort(suffixes, (a, b) -> {
            if (a.rank1 != b.rank1) return Integer.compare(a.rank1, b.rank1);
            return Integer.compare(a.rank2, b.rank2);
        });

        int[] ind = new int[n];
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank1 = suffixes[0].rank1;
            int prevRank2 = suffixes[0].rank2;
            suffixes[0].rank1 = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rank1 == prevRank1 && suffixes[i].rank2 == prevRank2) {
                    suffixes[i].rank1 = rank;
                } else {
                    prevRank1 = suffixes[i].rank1;
                    prevRank2 = suffixes[i].rank2;
                    suffixes[i].rank1 = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rank2 = (nextIndex < n) ? suffixes[ind[nextIndex]].rank1 : -1;
            }

            CustomSort.mergeSort(suffixes, (a, b) -> {
                if (a.rank1 != b.rank1) return Integer.compare(a.rank1, b.rank1);
                return Integer.compare(a.rank2, b.rank2);
            });
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    /**
     * Searches for occurrences of pattern in text using binary search over the suffix array.
     * Time Complexity: O(m log n).
     */
    public static IntArrayList search(String text, String pattern, int[] suffixArray) {
        IntArrayList matches = new IntArrayList();
        if (text == null || pattern == null || suffixArray == null || suffixArray.length == 0) return matches;
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) return matches;

        // Binary search for lower bound of pattern
        int low = 0, high = n - 1;
        int firstOcc = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffStart = suffixArray[mid];
            int cmp = comparePrefix(text, suffStart, pattern);
            if (cmp >= 0) {
                if (cmp == 0) firstOcc = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (firstOcc == -1) return matches;

        // Binary search for upper bound
        low = firstOcc;
        high = n - 1;
        int lastOcc = firstOcc;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffStart = suffixArray[mid];
            int cmp = comparePrefix(text, suffStart, pattern);
            if (cmp == 0) {
                lastOcc = mid;
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // Collect all occurrences
        for (int i = firstOcc; i <= lastOcc; i++) {
            matches.add(suffixArray[i]);
        }
        return matches;
    }

    private static int comparePrefix(String text, int start, String pattern) {
        int m = pattern.length();
        int n = text.length();
        int len = Math.min(m, n - start);
        for (int i = 0; i < len; i++) {
            char tc = text.charAt(start + i);
            char pc = pattern.charAt(i);
            if (tc != pc) return Character.compare(tc, pc);
        }
        if (n - start < m) return -1;
        return 0;
    }
}
