import java.util.*;

class Solution {
    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        // [left, right, weight, originalIndex]
        int[][] a = new int[n][4];

        for (int i = 0; i < n; i++) {
            a[i][0] = intervals.get(i).get(0);
            a[i][1] = intervals.get(i).get(1);
            a[i][2] = intervals.get(i).get(2);
            a[i][3] = i;
        }

        // Sort by starting position.
        Arrays.sort(a, (x, y) -> {
            if (x[0] != y[0]) return Integer.compare(x[0], y[0]);
            return Integer.compare(x[1], y[1]);
        });

        int[] starts = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = a[i][0];
        }

        // next[i] = first interval j such that starts[j] > a[i][1]
        int[] next = new int[n];

        for (int i = 0; i < n; i++) {
            next[i] = upperBound(starts, a[i][1]);
        }

        /*
         * dp[k][i] = best result using at most k intervals
         * among intervals i ... n-1.
         */
        Node[][] dp = new Node[5][n + 1];

        // IMPORTANT:
        // dp[0][i] must be initialized for every i.
        for (int i = 0; i <= n; i++) {
            dp[0][i] = new Node(0, new int[0]);
        }

        for (int k = 1; k <= 4; k++) {
            dp[k][n] = new Node(0, new int[0]);

            for (int i = n - 1; i >= 0; i--) {

                // Option 1: don't take interval i
                Node skip = dp[k][i + 1];

                // Option 2: take interval i
                Node nextNode = dp[k - 1][next[i]];

                int[] indices = insertSorted(
                    nextNode.indices,
                    a[i][3]
                );

                Node take = new Node(
                    nextNode.sum + a[i][2],
                    indices
                );

                dp[k][i] = better(take, skip);
            }
        }

        return dp[4][0].indices;
    }

    // First position where arr[pos] > target.
    private int upperBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    // Return the better result:
    // 1. Higher sum
    // 2. Lexicographically smaller indices
    private Node better(Node a, Node b) {
        if (a.sum != b.sum) {
            return a.sum > b.sum ? a : b;
        }

        return compareLexicographically(a.indices, b.indices) < 0
                ? a
                : b;
    }

    private int compareLexicographically(int[] a, int[] b) {
        int len = Math.min(a.length, b.length);

        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }

        return Integer.compare(a.length, b.length);
    }

    // Insert x into an already sorted array.
    private int[] insertSorted(int[] arr, int x) {
        int[] result = new int[arr.length + 1];

        int pos = 0;

        while (pos < arr.length && arr[pos] < x) {
            result[pos] = arr[pos];
            pos++;
        }

        result[pos] = x;

        while (pos < arr.length) {
            result[pos + 1] = arr[pos];
            pos++;
        }

        return result;
    }

    static class Node {
        long sum;
        int[] indices;

        Node(long sum, int[] indices) {
            this.sum = sum;
            this.indices = indices;
        }
    }
}
