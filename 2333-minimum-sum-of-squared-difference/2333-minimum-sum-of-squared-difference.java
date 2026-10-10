class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        int[] diff = new int[n];

        int max = 0;
        long sum = 0;

        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            max = Math.max(max, diff[i]);
            sum += diff[i];
        }

        long k = (long) k1 + k2;

        if (sum <= k) {
            return 0;
        }

        int left = 0, right = max;

        // Find the minimum possible maximum difference
        while (left < right) {
            int mid = left + (right - left) / 2;
            long required = 0;

            for (int d : diff) {
                if (d > mid) {
                    required += d - mid;
                }
            }

            if (required <= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        int target = left;
        long remaining = k;

        // Reduce all differences greater than target
        for (int i = 0; i < n; i++) {
            if (diff[i] > target) {
                remaining -= diff[i] - target;
                diff[i] = target;
            }
        }

        // Use remaining operations to reduce target-level differences
        for (int i = 0; i < n && remaining > 0; i++) {
            if (diff[i] == target && target > 0) {
                diff[i]--;
                remaining--;
            }
        }

        long result = 0;

        for (int d : diff) {
            result += (long) d * d;
        }

        return result;
    }
}