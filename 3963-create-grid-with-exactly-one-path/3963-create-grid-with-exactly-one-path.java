
import java.util.Arrays;

class Solution {
    public String[] createGrid(int m, int n) {
        char[][] grid = new char[m][n];

        // Fill the entire grid with obstacles
        for (int i = 0; i < m; i++) {
            Arrays.fill(grid[i], '#');
        }

        // Open the first row
        Arrays.fill(grid[0], '.');

        // Open the last column
        for (int i = 0; i < m; i++) {
            grid[i][n - 1] = '.';
        }

        // Convert each row into a String
        String[] result = new String[m];

        for (int i = 0; i < m; i++) {
            result[i] = new String(grid[i]);
        }

        return result;
    }
}
