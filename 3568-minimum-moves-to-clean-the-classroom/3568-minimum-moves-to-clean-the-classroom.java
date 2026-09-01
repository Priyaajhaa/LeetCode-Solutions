import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {
        int m = classroom.length;
        int n = classroom[0].length();

        int sr = -1, sc = -1;
        List<int[]> litter = new ArrayList<>();

        // Find starting position and litter
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                char ch = classroom[r].charAt(c);

                if (ch == 'S') {
                    sr = r;
                    sc = c;
                } else if (ch == 'L') {
                    litter.add(new int[]{r, c});
                }
            }
        }

        int k = litter.size();

        // No litter
        if (k == 0) {
            return 0;
        }

        // Assign an ID to every litter cell
        int[][] litterId = new int[m][n];

        for (int[] row : litterId) {
            Arrays.fill(row, -1);
        }

        for (int i = 0; i < k; i++) {
            int r = litter.get(i)[0];
            int c = litter.get(i)[1];
            litterId[r][c] = i;
        }

        int allCollected = (1 << k) - 1;

        // visited[row][col][mask][remainingEnergy]
        boolean[][][][] visited =
                new boolean[m][n][1 << k][energy + 1];

        Queue<State> queue = new ArrayDeque<>();

        // Initial state
        int initialMask = 0;

        if (litterId[sr][sc] != -1) {
            initialMask |= 1 << litterId[sr][sc];
        }

        visited[sr][sc][initialMask][energy] = true;

        queue.offer(new State(
                sr,
                sc,
                initialMask,
                energy,
                0
        ));

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            // All litter collected
            if (cur.mask == allCollected) {
                return cur.moves;
            }

            // No energy left
            if (cur.energy == 0) {
                continue;
            }

            // Try 4 directions
            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                // Outside grid
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                    continue;
                }

                // Obstacle
                if (classroom[nr].charAt(nc) == 'X') {
                    continue;
                }

                int newEnergy = cur.energy - 1;

                // Reset area
                if (classroom[nr].charAt(nc) == 'R') {
                    newEnergy = energy;
                }

                // Collect litter
                int newMask = cur.mask;
                int id = litterId[nr][nc];

                if (id != -1) {
                    newMask |= (1 << id);
                }

                // Visit new state
                if (!visited[nr][nc][newMask][newEnergy]) {
                    visited[nr][nc][newMask][newEnergy] = true;

                    queue.offer(new State(
                            nr,
                            nc,
                            newMask,
                            newEnergy,
                            cur.moves + 1
                    ));
                }
            }
        }

        // Impossible
        return -1;
    }

    static class State {
        int r;
        int c;
        int mask;
        int energy;
        int moves;

        State(int r, int c, int mask, int energy, int moves) {
            this.r = r;
            this.c = c;
            this.mask = mask;
            this.energy = energy;
            this.moves = moves;
        }
    }
}
