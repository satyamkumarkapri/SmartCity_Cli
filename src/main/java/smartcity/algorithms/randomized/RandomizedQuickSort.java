package smartcity.algorithms.randomized;

import java.util.Random;

/**
 * Randomized QuickSort with 3-Way Partitioning (Dutch National Flag)
 * for optimal handling of heavy duplicate keys.
 * Includes comparison/swap instrumentation and deterministic pivot comparison mode.
 * Expected Time: O(n log n). Worst-Case Time: O(n^2).
 */
public class RandomizedQuickSort {

    public static class SortMetrics {
        public long comparisons = 0;
        public long swaps = 0;
    }

    public static void sort(int[] a) {
        sort(a, new Random(), null);
    }

    public static void sort(int[] a, Random rng, SortMetrics metrics) {
        if (a == null || a.length <= 1) return;
        quickSort(a, 0, a.length - 1, true, rng, metrics);
    }

    public static void sortDeterministic(int[] a, SortMetrics metrics) {
        if (a == null || a.length <= 1) return;
        quickSort(a, 0, a.length - 1, false, null, metrics);
    }

    private static void quickSort(int[] a, int low, int high, boolean randomize, Random rng, SortMetrics metrics) {
        if (low >= high) return;

        if (randomize && rng != null) {
            int pIdx = low + rng.nextInt(high - low + 1);
            swap(a, low, pIdx, metrics);
        }

        // 3-Way Partitioning (Dutch National Flag)
        int pivot = a[low];
        int lt = low;     // a[low..lt-1] < pivot
        int gt = high;    // a[gt+1..high] > pivot
        int i = low + 1;  // a[lt..i-1] == pivot

        while (i <= gt) {
            if (metrics != null) metrics.comparisons++;
            if (a[i] < pivot) {
                swap(a, lt++, i++, metrics);
            } else if (a[i] > pivot) {
                swap(a, i, gt--, metrics);
            } else {
                i++;
            }
        }

        quickSort(a, low, lt - 1, randomize, rng, metrics);
        quickSort(a, gt + 1, high, randomize, rng, metrics);
    }

    public static void sort(double[] a) {
        if (a == null || a.length <= 1) return;
        quickSort(a, 0, a.length - 1, true, new Random());
    }

    private static void quickSort(double[] a, int low, int high, boolean randomize, Random rng) {
        if (low >= high) return;
        if (randomize && rng != null) {
            int pIdx = low + rng.nextInt(high - low + 1);
            double tmp = a[low]; a[low] = a[pIdx]; a[pIdx] = tmp;
        }
        double pivot = a[low];
        int lt = low;
        int gt = high;
        int i = low + 1;
        while (i <= gt) {
            if (a[i] < pivot) {
                double tmp = a[lt]; a[lt++] = a[i]; a[i++] = tmp;
            } else if (a[i] > pivot) {
                double tmp = a[i]; a[i] = a[gt]; a[gt--] = tmp;
            } else {
                i++;
            }
        }
        quickSort(a, low, lt - 1, randomize, rng);
        quickSort(a, gt + 1, high, randomize, rng);
    }

    private static void swap(int[] a, int i, int j, SortMetrics metrics) {
        if (i == j) return;
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        if (metrics != null) metrics.swaps++;
    }
}
