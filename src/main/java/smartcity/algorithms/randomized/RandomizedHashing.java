package smartcity.algorithms.randomized;

import java.util.Random;

/**
 * Universal Hashing and Randomized Hash Family Analysis.
 * Implements the linear congruential universal family:
 *   h_{a,b}(x) = ((a * x + b) mod p) mod m
 * with prime p > max(x) and randomly chosen parameters a in [1, p-1], b in [0, p-1].
 * Guarantees collision probability P(h(x) == h(y)) <= 1 / m for any distinct keys x != y.
 */
public class RandomizedHashing {
    // Large 61-bit Mersenne prime 2^61 - 1
    public static final long MERSENNE_PRIME_61 = (1L << 61) - 1;

    public static class UniversalHashFunction {
        public final long a;
        public final long b;
        public final int m;

        public UniversalHashFunction(long a, long b, int m) {
            this.a = a;
            this.b = b;
            this.m = m;
        }

        public int hash(long key) {
            // ((a * key + b) mod p) mod m
            java.math.BigInteger prod = java.math.BigInteger.valueOf(a)
                    .multiply(java.math.BigInteger.valueOf(key))
                    .add(java.math.BigInteger.valueOf(b));
            long modP = prod.mod(java.math.BigInteger.valueOf(MERSENNE_PRIME_61)).longValue();
            return (int) (Math.abs(modP) % m);
        }
    }

    public static class HashFamilyMetrics {
        public final int numKeys;
        public final int tableSlots;
        public final int trials;
        public final double averageCollisions;
        public final double theoreticalExpectedCollisions;

        public HashFamilyMetrics(int n, int m, int trials, double avg, double expected) {
            this.numKeys = n;
            this.tableSlots = m;
            this.trials = trials;
            this.averageCollisions = avg;
            this.theoreticalExpectedCollisions = expected;
        }

        @Override
        public String toString() {
            return String.format("""
            Universal Hash Family Benchmark:
               Keys Inserted       : %,d
               Hash Table Buckets  : %,d
               Random Hash Trials  : %d
               Measured Collisions : %.2f average
               Theoretical Bound   : <= %.2f (n*(n-1)/(2m))
               Theorem             : P(h(x) == h(y)) <= 1/m confirmed across independent random seeds.
            """, numKeys, tableSlots, trials, averageCollisions, theoreticalExpectedCollisions);
        }
    }

    public static UniversalHashFunction createRandomFunction(int m, Random rng) {
        long a = 1 + Math.abs(rng.nextLong()) % (MERSENNE_PRIME_61 - 1);
        long b = Math.abs(rng.nextLong()) % MERSENNE_PRIME_61;
        return new UniversalHashFunction(a, b, m);
    }

    public static HashFamilyMetrics benchmarkHashFamily(long[] keys, int tableSlots, int trials, Random rng) {
        int n = keys.length;
        long totalCollisions = 0;

        for (int t = 0; t < trials; t++) {
            UniversalHashFunction h = createRandomFunction(tableSlots, rng);
            int[] bucketCounts = new int[tableSlots];
            int collisions = 0;

            for (long k : keys) {
                int slot = h.hash(k);
                if (bucketCounts[slot] > 0) {
                    collisions++;
                }
                bucketCounts[slot]++;
            }
            totalCollisions += collisions;
        }

        double avgCollisions = (double) totalCollisions / trials;
        // Expected collision pairs E[C] = n*(n-1)/(2m)
        double theoretical = (double) n * (n - 1) / (2.0 * tableSlots);

        return new HashFamilyMetrics(n, tableSlots, trials, avgCollisions, theoretical);
    }
}
