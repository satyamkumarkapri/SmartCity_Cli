package smartcityx.algorithms.parallel;

import java.util.List;
import java.util.Random;
import smartcityx.model.ServiceRequest;

public class RandomizedQuickSort {
    private static Random rand = new Random();

    public static void sortRequests(List<ServiceRequest> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            sortRequests(list, low, pi - 1);
            sortRequests(list, pi + 1, high);
        }
    }

    private static int partition(List<ServiceRequest> list, int low, int high) {
        int randomPivot = low + rand.nextInt(high - low + 1);
        swap(list, randomPivot, high);
        
        ServiceRequest pivot = list.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (list.get(j).getPriority().compareTo(pivot.getPriority()) >= 0) { 
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private static void swap(List<ServiceRequest> list, int i, int j) {
        ServiceRequest temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
