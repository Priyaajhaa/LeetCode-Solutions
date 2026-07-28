class Solution {
    public String smallestPalindrome(String s) {
        int[] freq = new int[26];

        for (char ch : s.toCharArray()) {
            freq[ch - 'a']++;
        }

        StringBuilder firstHalf = new StringBuilder();
        char middle = 0;

        for (int i = 0; i < 26; i++) {
            if ((freq[i] & 1) == 1) {
                middle = (char) ('a' + i);
            }

            for (int j = 0; j < freq[i] / 2; j++) {
                firstHalf.append((char) ('a' + i));
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(firstHalf);

        if (middle != 0) {
            result.append(middle);
        }

        result.append(new StringBuilder(firstHalf).reverse());

        return result.toString();
    }
}