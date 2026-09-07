class Solution {
    public int distinctSubseqII(String s) {
        final long MOD = 1_000_000_007L;

        // dp[i] = number of distinct subsequences (including empty)
        // using the first i characters.
        long[] dp = new long[s.length() + 1];
        dp[0] = 1; // empty subsequence

        // Last position where each character occurred.
        // Store dp value from just before that occurrence.
        long[] last = new long[26];

        for (int i = 1; i <= s.length(); i++) {
            int c = s.charAt(i - 1) - 'a';

            // Double the subsequences by either taking or not taking
            // the current character.
            dp[i] = (2 * dp[i - 1]) % MOD;

            // Remove duplicates caused by a previous occurrence
            // of the same character.
            dp[i] = (dp[i] - last[c] + MOD) % MOD;

            // Save the number of subsequences before this occurrence.
            last[c] = dp[i - 1];
        }

        // Remove the empty subsequence.
        return (int) ((dp[s.length()] - 1 + MOD) % MOD);
    }
}
