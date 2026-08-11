class Solution {
    public int missingInteger(int[] nums) {
        // Find the sum of the longest sequential prefix
        int sum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] + 1) {
                sum += nums[i];
            } else {
                break;
            }
        }

        // Find the smallest integer >= sum that is not in nums
        int x = sum;

        while (contains(nums, x)) {
            x++;
        }

        return x;
    }

    private boolean contains(int[] nums, int x) {
        for (int num : nums) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }
}