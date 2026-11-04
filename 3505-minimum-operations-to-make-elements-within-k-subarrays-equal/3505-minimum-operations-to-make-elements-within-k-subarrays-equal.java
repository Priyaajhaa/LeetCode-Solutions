import java.util.*;

class Solution {
    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;
        long[] cost = computeCosts(nums, x);   // cost[i] for window starting at i, length n-x+1

        final long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[k + 1][n + 1];
        for (long[] row : dp) Arrays.fill(row, INF);
        for (int i = 0; i <= n; i++) dp[0][i] = 0;

        for (int j = 1; j <= k; j++) {
            for (int i = 1; i <= n; i++) {
                dp[j][i] = dp[j][i - 1];
                if (i >= x && dp[j - 1][i - x] < INF) {
                    dp[j][i] = Math.min(dp[j][i], dp[j - 1][i - x] + cost[i - x]);
                }
            }
        }
        return dp[k][n];
    }

    private long[] computeCosts(int[] nums, int x) {
        int n = nums.length;
        long[] cost = new long[n - x + 1];

        Comparator<long[]> cmp = (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]);
        TreeSet<long[]> low = new TreeSet<>(cmp);   // smaller half, size = ceil(x/2)
        TreeSet<long[]> high = new TreeSet<>(cmp);  // larger half
        long sumLow = 0, sumHigh = 0;

        for (int i = 0; i < n; i++) {
            // add nums[i]
            long[] cur = new long[]{nums[i], i};
            if (low.isEmpty() || cmp.compare(cur, low.last()) <= 0) {
                low.add(cur);
                sumLow += nums[i];
            } else {
                high.add(cur);
                sumHigh += nums[i];
            }

            // remove the element leaving the window
            if (i >= x) {
                long[] out = new long[]{nums[i - x], i - x};
                if (low.contains(out)) {
                    low.remove(out);
                    sumLow -= nums[i - x];
                } else {
                    high.remove(out);
                    sumHigh -= nums[i - x];
                }
            }

            // rebalance so that low.size() == high.size() or high.size() + 1
            while (low.size() > high.size() + 1) {
                long[] t = low.pollLast();
                sumLow -= t[0];
                high.add(t);
                sumHigh += t[0];
            }
            while (low.size() < high.size()) {
                long[] t = high.pollFirst();
                sumHigh -= t[0];
                low.add(t);
                sumLow += t[0];
            }

            if (i >= x - 1) {
                long med = low.last()[0];
                cost[i - x + 1] = med * low.size() - sumLow + sumHigh - med * high.size();
            }
        }
        return cost;
    }
}