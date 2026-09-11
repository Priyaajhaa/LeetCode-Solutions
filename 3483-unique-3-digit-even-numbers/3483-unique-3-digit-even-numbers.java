class Solution {
    public int totalNumbers(int[] digits) {
        int[] freq = new int[10];

        for (int digit : digits) {
            freq[digit]++;
        }

        int count = 0;

        // Choose the hundreds digit (cannot be 0)
        for (int i = 1; i <= 9; i++) {
            if (freq[i] == 0) continue;
            freq[i]--;

            // Choose the tens digit
            for (int j = 0; j <= 9; j++) {
                if (freq[j] == 0) continue;
                freq[j]--;

                // Last digit must be even
                for (int k = 0; k <= 8; k += 2) {
                    if (freq[k] > 0) {
                        count++;
                    }
                }

                freq[j]++;
            }

            freq[i]++;
        }

        return count;
    }
}
