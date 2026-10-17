import java.util.*;

class Solution {
    public int minSwaps(int[] nums) {
        int n = nums.length;
        int[][] arr = new int[n][2];

        for (int i = 0; i < n; i++) {
            arr[i][0] = nums[i];
            arr[i][1] = digitSum(nums[i]);
        }

        Arrays.sort(arr, (a, b) -> {
            if (a[1] != b[1]) {
                return a[1] - b[1];
            }
            return a[0] - b[0];
        });

        Map<Integer, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(arr[i][0], i);
        }

        int[] sorted = new int[n];
        for (int i = 0; i < n; i++) {
            sorted[i] = arr[i][0];
        }

        int swaps = 0;

        for (int i = 0; i < n; i++) {
            while (nums[i] != sorted[i]) {
                int j = index.get(nums[i]);

                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;

                swaps++;
            }
        }

        return swaps;
    }

    private int digitSum(int num) {
        int sum = 0;

        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }

        return sum;
    }
}