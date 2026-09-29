class Solution {
    public boolean hasValidPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // A valid path must start with '(' and end with ')'
        if (grid[0][0] == ')' || grid[m - 1][n - 1] == '(') {
            return false;
        }

        // Number of cells in the path must be even
        if ((m + n - 1) % 2 != 0) {
            return false;
        }

        // dp[i][j][balance] = can we reach (i,j) with this balance?
        boolean[][][] dp = new boolean[m][n][m + n];

        dp[0][0][1] = true;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                int change = (grid[i][j] == '(') ? 1 : -1;

                for (int balance = 0; balance < m + n; balance++) {

                    int prevBalance = balance - change;

                    if (prevBalance < 0 || prevBalance >= m + n) {
                        continue;
                    }

                    // From top
                    if (i > 0 && dp[i - 1][j][prevBalance]) {
                        dp[i][j][balance] = true;
                    }

                    // From left
                    if (j > 0 && dp[i][j - 1][prevBalance]) {
                        dp[i][j][balance] = true;
                    }
                }
            }
        }

        return dp[m - 1][n - 1][0];
    }
}
