class Solution {
    private static final long MOD = 1_000_000_007L;

    public int numberOfSets(int n, int k) {
        int max = n + k - 1;

        long[] fact = new long[max + 1];
        long[] invFact = new long[max + 1];

        // factorials
        fact[0] = 1;
        for (int i = 1; i <= max; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }

        // inverse factorials
        invFact[max] = modPow(fact[max], MOD - 2);

        for (int i = max - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }

        // C(n + k - 1, 2k)
        return (int) combination(max, 2 * k, fact, invFact);
    }

    private long combination(int n, int r, long[] fact, long[] invFact) {
        if (r < 0 || r > n) {
            return 0;
        }

        return fact[n]
                * invFact[r] % MOD
                * invFact[n - r] % MOD;
    }

    private long modPow(long a, long e) {
        long result = 1;

        while (e > 0) {
            if ((e & 1) == 1) {
                result = result * a % MOD;
            }

            a = a * a % MOD;
            e >>= 1;
        }

        return result;
    }
}
