package smartcityx.algorithms.parallel;

import java.math.BigInteger;

public class MillerRabin {
    public static boolean isPrime(long n, int k) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;

        BigInteger num = BigInteger.valueOf(n);
        return num.isProbablePrime(k);
    }
}
