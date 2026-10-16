package smartcity.structures;

import java.util.Comparator;

/**
 * Custom sorting algorithms (MergeSort, QuickSort, CountingSort)
 * implemented without delegating to java.util.Arrays.sort or java.util.Collections.sort.
 */
public class CustomSort {

    /**
     * Generic stable MergeSort for Object arrays using Comparator.
     */
    @SuppressWarnings("unchecked")
    public static <T> void mergeSort(T[] array, Comparator<? super T> comp) {
        if (array == null || array.length <= 1) return;
        T[] aux = (T[]) new Object[array.length];
        mergeSortInternal(array, aux, 0, array.length - 1, comp);
    }

    private static <T> void mergeSortInternal(T[] a, T[] aux, int low, int high, Comparator<? super T> comp) {
        if (low >= high) return;
        int mid = low + (high - low) / 2;
        mergeSortInternal(a, aux, low, mid, comp);
        mergeSortInternal(a, aux, mid + 1, high, comp);
        merge(a, aux, low, mid, high, comp);
    }

    private static <T> void merge(T[] a, T[] aux, int low, int mid, int high, Comparator<? super T> comp) {
        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > high) {
                a[k] = aux[i++];
            } else if (comp.compare(aux[j], aux[i]) < 0) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    /**
     * Primitive int Dual-Pivot/Standard QuickSort.
     */
    public static void quickSort(int[] a) {
        if (a == null || a.length <= 1) return;
        quickSortInternal(a, 0, a.length - 1);
    }

    private static void quickSortInternal(int[] a, int low, int high) {
        if (low >= high) return;
        int p = partition(a, low, high);
        quickSortInternal(a, low, p - 1);
        quickSortInternal(a, p + 1, high);
    }

    private static int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        int temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;
        return i + 1;
    }

    /**
     * Primitive double QuickSort.
     */
    public static void quickSort(double[] a) {
        if (a == null || a.length <= 1) return;
        quickSortInternal(a, 0, a.length - 1);
    }

    private static void quickSortInternal(double[] a, int low, int high) {
        if (low >= high) return;
        double pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                double temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        double temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;
        int p = i + 1;
        quickSortInternal(a, low, p - 1);
        quickSortInternal(a, p + 1, high);
    }
}
