class Solution {
    public long[] resultArray(int[] nums, int k) {
        int n = nums.length;
        long[] result = new long[k];

        long[] dp = new long[k];

        for (int num : nums) {
            int val = num % k;
            long[] next = new long[k];

            // Start a new subarray
            next[val]++;

            // Extend previous subarrays
            for (int r = 0; r < k; r++) {
                if (dp[r] != 0) {
                    int nr = (int) ((long) r * val % k);
                    next[nr] += dp[r];
                }
            }

            // Add counts
            for (int r = 0; r < k; r++) {
                result[r] += next[r];
            }

            dp = next;
        }

        return result;
    }
}
