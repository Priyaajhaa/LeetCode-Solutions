class Solution {
    public boolean winnerSquareGame(int n) {
        boolean[] dp = new boolean[n + 1];

        // dp[i] = true means the current player can win with i stones.
        dp[0] = false;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                // If removing j*j leaves a losing position,
                // current player can force a win.
                if (!dp[i - j * j]) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[n];
    }
}