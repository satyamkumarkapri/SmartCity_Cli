package smartcity.algorithms.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Parallel MergeSort Primitive using bounded ForkJoinPool tasks.
 * Divides array recursively down to threshold, sorts halves in parallel,
 * and merges the sorted segments.
 * Work T1 = O(n log n).
 * Span Tinf = O(n) (or O(log^2 n) with parallel merge).
 */
public class ParallelSort {
    public static final int THRESHOLD = 2000;
    private static final ForkJoinPool POOL = new ForkJoinPool();

    public static void parallelSort(double[] a) {
        if (a == null || a.length <= 1) return;
        double[] aux = new double[a.length];
        POOL.invoke(new SortTask(a, aux, 0, a.length - 1));
    }

    public static void sort(double[] a) {
        parallelSort(a);
    }


    private static class SortTask extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final double[] a;
        private final double[] aux;
        private final int low;
        private final int high;

        public SortTask(double[] a, double[] aux, int low, int high) {
            this.a = a;
            this.aux = aux;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low <= THRESHOLD) {
                sequentialMergeSort(a, aux, low, high);
                return;
            }

            int mid = low + (high - low) / 2;
            SortTask left = new SortTask(a, aux, low, mid);
            SortTask right = new SortTask(a, aux, mid + 1, high);
            invokeAll(left, right);
            merge(a, aux, low, mid, high);
        }
    }

    public static void sequentialMergeSort(double[] a, double[] aux, int low, int high) {
        if (low >= high) return;
        int mid = low + (high - low) / 2;
        sequentialMergeSort(a, aux, low, mid);
        sequentialMergeSort(a, aux, mid + 1, high);
        merge(a, aux, low, mid, high);
    }

    private static void merge(double[] a, double[] aux, int low, int mid, int high) {
        System.arraycopy(a, low, aux, low, high - low + 1);
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
}
