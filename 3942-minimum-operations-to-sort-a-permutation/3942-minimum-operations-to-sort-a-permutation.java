
import java.util.*;

class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        int zeroIndex = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                zeroIndex = i;
                break;
            }
        }

        // BFS over 2n possible transformations:
        // orientation 0: rotations of nums
        // orientation 1: rotations of reversed nums
        int[][] dist = new int[2][n];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        dist[0][0] = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int orientation = curr[0];
            int k = curr[1];
            int d = dist[orientation][k];

            // Operation 1: Rotate left by one
            int nextK = (k + 1) % n;
            if (dist[orientation][nextK] == -1) {
                dist[orientation][nextK] = d + 1;
                queue.offer(new int[]{orientation, nextK});
            }

            // Operation 2: Reverse the entire array
            int nextOrientation = 1 - orientation;
            nextK = (n - k) % n;

            if (dist[nextOrientation][nextK] == -1) {
                dist[nextOrientation][nextK] = d + 1;
                queue.offer(new int[]{nextOrientation, nextK});
            }
        }

        int ans = Integer.MAX_VALUE;

        // Check rotations of the original array
        int k = zeroIndex;
        boolean sorted = true;

        for (int i = 0; i < n; i++) {
            if (nums[(k + i) % n] != i) {
                sorted = false;
                break;
            }
        }

        if (sorted) {
            ans = Math.min(ans, dist[0][k]);
        }

        // Check rotations of the reversed array
        k = (n - 1 - zeroIndex + n) % n;
        sorted = true;

        for (int i = 0; i < n; i++) {
            int index = (k + i) % n;
            int value = nums[n - 1 - index];

            if (value != i) {
                sorted = false;
                break;
            }
        }

        if (sorted) {
            ans = Math.min(ans, dist[1][k]);
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
