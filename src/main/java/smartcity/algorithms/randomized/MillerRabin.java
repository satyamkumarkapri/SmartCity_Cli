package smartcity.algorithms.randomized;

import smartcity.model.PrimeTestResult;
import smartcity.structures.DynamicArray;

import java.math.BigInteger;
import java.util.Random;

/**
 * Miller-Rabin Primality Testing with overflow-safe modular exponentiation.
 * Provides:
 * 1. 100% Deterministic testing for 64-bit integers using proven minimal witness sets.
 * 2. Probabilistic Monte Carlo testing for arbitrary large numbers with error bound <= (1/4)^k.
 * Correctly identifies Carmichael numbers (561, 1105, 1729, etc.) as composite.
 */
public class MillerRabin {

    // Proven deterministic witness set for 64-bit integers (n < 2^64)
    public static final long[] DETERMINISTIC_WITNESSES_64 = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37
    };

    // Minimal deterministic witness set for 32-bit integers (n < 2^32)
    public static final long[] DETERMINISTIC_WITNESSES_32 = {
            2, 7, 61
    };

    /**
     * Tests primality deterministically using optimal witness sets.
     */
    public static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if ((n & 1) == 0) return false;

        long[] bases = (n <= 0xFFFFFFFFL) ? DETERMINISTIC_WITNESSES_32 : DETERMINISTIC_WITNESSES_64;
        for (long a : bases) {
            if (n <= a) break;
            if (!testWitness(n, a)) {
                return false; // Composite witness confirmed
            }
        }
        return true;
    }

    /**
     * Probabilistic Miller-Rabin test using k random witness rounds.
     */
    public static boolean isPrimeProbabilistic(long n, int k, Random rng) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if ((n & 1) == 0) return false;

        for (int i = 0; i < k; i++) {
            long a = 2 + (Math.abs(rng.nextLong()) % (n - 3));
            if (!testWitness(n, a)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Full execution returning structured result with witness list and error bound.
     */
    public static PrimeTestResult testNumber(long n, int rounds) {
        DynamicArray<Long> usedWitnesses = new DynamicArray<>();
        if (n <= 1) {
            return new PrimeTestResult(true, "Miller-Rabin", null,
                    "Values <= 1 are not prime by definition.", n, false, true, 0, usedWitnesses, 0.0);
        }
        if (n == 2 || n == 3) {
            return new PrimeTestResult(true, "Miller-Rabin", null,
                    "Small foundational prime.", n, true, true, 1, usedWitnesses, 0.0);
        }
        if ((n & 1) == 0) {
            return new PrimeTestResult(true, "Miller-Rabin", null,
                    "Even integer > 2 is composite (divisible by 2).", n, false, true, 1, usedWitnesses, 0.0);
        }

        boolean deterministic = (rounds <= 0 || rounds >= 12);
        long[] bases = DETERMINISTIC_WITNESSES_64;

        for (long a : bases) {
            if (n <= a) break;
            usedWitnesses.add(a);
            if (!testWitness(n, a)) {
                return new PrimeTestResult(true, "Miller-Rabin", null,
                        "Base a=" + a + " proved that n is composite (witness found).",
                        n, false, true, usedWitnesses.size(), usedWitnesses, 0.0);
            }
        }

        double errorBound = Math.pow(0.25, usedWitnesses.size());
        return new PrimeTestResult(true, "Miller-Rabin", null,
                "Passed all witness tests. Guaranteed prime under deterministic bases.",
                n, true, deterministic, usedWitnesses.size(), usedWitnesses, errorBound);
    }

    /**
     * Single witness test for base a.
     * Returns true if n passes (likely prime), false if n is definitely composite.
     */
    public static boolean testWitness(long n, long a) {
        // Decompose n - 1 = 2^s * d with d odd
        long d = n - 1;
        int s = 0;
        while ((d & 1) == 0) {
            d >>= 1;
            s++;
        }

        long x = modPow(a, d, n);
        if (x == 1 || x == n - 1) return true;

        for (int r = 1; r < s; r++) {
            x = modMul(x, x, n);
            if (x == n - 1) return true;
        }
        return false;
    }

    /**
     * Overflow-safe modular multiplication: (a * b) % mod.
     */
    public static long modMul(long a, long b, long mod) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

    /**
     * Overflow-safe modular exponentiation: (base^exp) % mod.
     */
    public static long modPow(long base, long exp, long mod) {
        long res = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = modMul(res, base, mod);
            }
            base = modMul(base, base, mod);
            exp >>= 1;
        }
        return res;
    }
}
