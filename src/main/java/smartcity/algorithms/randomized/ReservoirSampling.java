package smartcity.algorithms.randomized;

import smartcity.structures.DynamicArray;

import java.util.Random;

/**
 * Reservoir Sampling (Algorithm R).
 * Uniformly samples k items from a data stream of unknown or infinite length in a single pass.
 * Space Complexity: O(k) for the reservoir buffer.
 * Time Complexity: O(n) where n is total elements streamed.
 *
 * Mathematical Guarantee:
 * Every element among the n processed stream items has an equal probability
 * exactly equal to min(1.0, k / n) of being included in the final reservoir.
 */
public class ReservoirSampling<T> {
    private final int k;
    private final Random rng;
    private final DynamicArray<T> reservoir;
    private long itemsSeen;

    public ReservoirSampling(int k, Random rng) {
        if (k < 0) throw new IllegalArgumentException("Sample size k must be non-negative");
        this.k = k;
        this.rng = rng != null ? rng : new Random();
        this.reservoir = new DynamicArray<>(Math.max(1, k));
        this.itemsSeen = 0;
    }

    public ReservoirSampling(int k) {
        this(k, new Random());
    }

    /**
     * Incrementally processes an incoming element from the stream.
     */
    public void feed(T item) {
        if (k == 0) {
            itemsSeen++;
            return;
        }

        if (itemsSeen < k) {
            // Fill initial reservoir buffer
            reservoir.add(item);
        } else {
            // Replace existing element with probability k / (itemsSeen + 1)
            long j = Math.abs(rng.nextLong()) % (itemsSeen + 1);
            if (j < k) {
                reservoir.set((int) j, item);
            }
        }
        itemsSeen++;
    }

    /**
     * Batch processes an entire stream / iterable.
     */
    public static <E> DynamicArray<E> sample(Iterable<E> stream, int k, Random rng) {
        ReservoirSampling<E> sampler = new ReservoirSampling<>(k, rng);
        for (E item : stream) {
            sampler.feed(item);
        }
        return sampler.getSample();
    }

    public DynamicArray<T> getSample() {
        return reservoir;
    }

    public long getItemsSeen() {
        return itemsSeen;
    }

    public int getSampleSize() {
        return reservoir.size();
    }
}
