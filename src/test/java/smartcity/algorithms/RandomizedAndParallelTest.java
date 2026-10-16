package smartcity.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.algorithms.parallel.BlellochScan;
import smartcity.algorithms.parallel.ParallelReduce;
import smartcity.algorithms.parallel.ParallelSort;
import smartcity.algorithms.randomized.MillerRabin;
import smartcity.algorithms.randomized.RandomizedHashing;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.randomized.ReservoirSampling;
import smartcity.structures.DynamicArray;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module 6: Randomized and Parallel Algorithms Tests")
public class RandomizedAndParallelTest {

    @Test
    @DisplayName("Randomized QuickSort: sorted, reversed, duplicate-heavy, and random arrays")
    public void testRandomizedQuickSort() {
        Random rng = new Random(12345);

        // Sorted array
        int[] sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        RandomizedQuickSort.sort(sorted, rng, null);
        assertSorted(sorted);

        // Reversed array
        int[] reversed = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        RandomizedQuickSort.sort(reversed, rng, null);
        assertSorted(reversed);

        // Heavy duplicates (Dutch National Flag)
        int[] duplicates = {5, 2, 5, 5, 1, 5, 9, 2, 5, 5, 1};
        RandomizedQuickSort.sort(duplicates, rng, null);
        assertSorted(duplicates);

        // Random large array
        int[] randomArr = new int[1000];
        for (int i = 0; i < randomArr.length; i++) randomArr[i] = rng.nextInt(500);
        RandomizedQuickSort.sort(randomArr, rng, null);
        assertSorted(randomArr);
    }

    private void assertSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            assertTrue(a[i] <= a[i + 1], "Array not sorted at index " + i);
        }
    }

    @Test
    @DisplayName("Miller-Rabin: Primes, composites, even numbers, and Carmichael numbers")
    public void testMillerRabin() {
        // Small and known primes
        assertTrue(MillerRabin.isPrime(2));
        assertTrue(MillerRabin.isPrime(3));
        assertTrue(MillerRabin.isPrime(5));
        assertTrue(MillerRabin.isPrime(17));
        assertTrue(MillerRabin.isPrime(1000000007L));

        // Composites and edge cases
        assertFalse(MillerRabin.isPrime(0));
        assertFalse(MillerRabin.isPrime(1));
        assertFalse(MillerRabin.isPrime(4));
        assertFalse(MillerRabin.isPrime(9));
        assertFalse(MillerRabin.isPrime(100));
        assertFalse(MillerRabin.isPrime(1000000005L));

        // Carmichael numbers (pass Fermat test for all coprime bases, but FAIL Miller-Rabin!)
        assertFalse(MillerRabin.isPrime(561), "Carmichael number 561 must be detected as composite");
        assertFalse(MillerRabin.isPrime(1105), "Carmichael number 1105 must be detected as composite");
        assertFalse(MillerRabin.isPrime(1729), "Carmichael number 1729 must be detected as composite");
    }

    @Test
    @DisplayName("Universal Hashing: Collision bounds verification")
    public void testUniversalHashing() {
        Random rng = new Random(42);
        long[] keys = new long[200];
        for (int i = 0; i < keys.length; i++) keys[i] = i * 17L + 13L;

        RandomizedHashing.HashFamilyMetrics metrics =
                RandomizedHashing.benchmarkHashFamily(keys, 100, 10, rng);
        // Universal property guarantees average collisions stay close to theoretical bound
        assertTrue(metrics.averageCollisions > 0);
        assertTrue(metrics.averageCollisions <= metrics.theoreticalExpectedCollisions * 2.5);
    }

    @Test
    @DisplayName("Reservoir Sampling: stream of unknown length, k=0, stream < k")
    public void testReservoirSampling() {
        Random rng = new Random(42);

        // k = 0
        ReservoirSampling<Integer> k0 = new ReservoirSampling<>(0, rng);
        k0.feed(1); k0.feed(2);
        assertEquals(0, k0.getSampleSize());

        // stream < k
        ReservoirSampling<Integer> smallStream = new ReservoirSampling<>(5, rng);
        smallStream.feed(10); smallStream.feed(20);
        assertEquals(2, smallStream.getSampleSize());

        // stream of 100 items sampled to k = 10
        DynamicArray<Integer> stream = new DynamicArray<>();
        for (int i = 0; i < 100; i++) stream.add(i);
        DynamicArray<Integer> sample = ReservoirSampling.sample(stream, 10, rng);
        assertEquals(10, sample.size());
    }

    @Test
    @DisplayName("Parallel Reduce, Blelloch Scan, and Parallel Sort")
    public void testParallelPrimitives() {
        double[] data = {1, 2, 3, 4, 5, 6, 7, 8};

        // Parallel reduce
        double sum = ParallelReduce.computeSequential(data, ParallelReduce.Op.SUM);
        assertEquals(36.0, sum);
        double min = ParallelReduce.computeSequential(data, ParallelReduce.Op.MIN);
        assertEquals(1.0, min);
        double max = ParallelReduce.computeSequential(data, ParallelReduce.Op.MAX);
        assertEquals(8.0, max);

        // Blelloch prefix scan
        double[] scan = BlellochScan.exclusiveScan(data);
        assertTrue(BlellochScan.verifyScan(scan, data));
        assertEquals(0.0, scan[0]);
        assertEquals(1.0, scan[1]);
        assertEquals(3.0, scan[2]); // 1 + 2
        assertEquals(6.0, scan[3]); // 1 + 2 + 3

        // Parallel MergeSort
        double[] toSort = {9.0, 3.0, 7.0, 1.0, 5.0, 2.0};
        ParallelSort.parallelSort(toSort);
        for (int i = 0; i < toSort.length - 1; i++) {
            assertTrue(toSort[i] <= toSort[i + 1]);
        }
    }
}
