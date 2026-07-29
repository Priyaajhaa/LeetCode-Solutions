import java.util.*;

class Solution {
    private static final long LIMIT = 1_000_001L;

    public String smallestPalindrome(String s, int k) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        int[] half = new int[26];
        int halfLen = 0;
        char mid = 0;

        for (int i = 0; i < 26; i++) {
            if ((freq[i] & 1) == 1) mid = (char) ('a' + i);
            half[i] = freq[i] / 2;
            halfLen += half[i];
        }

        if (countWays(half, halfLen) < k) return "";

        StringBuilder first = new StringBuilder();

        for (int pos = 0; pos < halfLen; pos++) {
            for (int c = 0; c < 26; c++) {
                if (half[c] == 0) continue;

                half[c]--;
                long ways = countWays(half, halfLen - pos - 1);

                if (ways >= k) {
                    first.append((char) ('a' + c));
                    break;
                } else {
                    k -= ways;
                    half[c]++;
                }
            }
        }

        String second = new StringBuilder(first).reverse().toString();
        if (mid == 0) return first.toString() + second;
        return first.toString() + mid + second;
    }

    private long countWays(int[] cnt, int total) {
        long ans = 1;
        int remaining = total;

        for (int c : cnt) {
            if (c == 0) continue;
            ans = multiplyChoose(ans, remaining, c);
            if (ans >= LIMIT) return LIMIT;
            remaining -= c;
        }
        return ans;
    }

    private long multiplyChoose(long cur, int n, int r) {
        if (r == 0) return cur;
        r = Math.min(r, n - r);

        long res = cur;

        for (int i = 1; i <= r; i++) {
            long num = n - r + i;
            long den = i;

            long g = gcd(num, den);
            num /= g;
            den /= g;

            g = gcd(res, den);
            res /= g;
            den /= g;

            if (res > LIMIT / num) return LIMIT;
            res *= num;
            res /= den;

            if (res >= LIMIT) return LIMIT;
        }

        return res;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}