class Solution {
    public boolean stoneGameIX(int[] stones) {
        int[] cnt = new int[3];

        for (int x : stones) {
            cnt[x % 3]++;
        }

        // If there are no stones with remainder 1 or 2,
        // Alice must eventually take a multiple of 3 and lose.
        if (cnt[1] == 0 && cnt[2] == 0) {
            return false;
        }

        // If the number of multiples of 3 is even:
        // Alice wins iff there is at least one 1 and one 2.
        if (cnt[0] % 2 == 0) {
            return cnt[1] > 0 && cnt[2] > 0;
        }

        // If the number of multiples of 3 is odd:
        // Alice wins iff the counts of 1 and 2 differ by at least 3.
        return Math.abs(cnt[1] - cnt[2]) >= 3;
    }
}