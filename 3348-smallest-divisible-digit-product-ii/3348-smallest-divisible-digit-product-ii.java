
 
class Solution {
    // exponents of 2,3,5,7 contributed by each digit 0..9 (digit 0 unused)
    static final int[] EXP2 = {0,0,1,0,2,0,1,0,3,0};
    static final int[] EXP3 = {0,0,0,1,0,0,1,0,0,2};
    static final int[] EXP5 = {0,0,0,0,0,1,0,0,0,0};
    static final int[] EXP7 = {0,0,0,0,0,0,0,1,0,0};
 
    private static int ceilDiv(int x, int y) {
        return (x + y - 1) / y;
    }
 
    // minimum number of digits (using only digits contributing to a/b: 2,3,4,6,8,9)
    // needed so exponent-of-2 sum >= ra AND exponent-of-3 sum >= rb.
    // digit 6 contributes to both simultaneously (shared), 8 is best pure "2" (value 3),
    // 9 is best pure "3" (value 2).
    private static int minAB(int ra, int rb) {
        if (ra <= 0 && rb <= 0) return 0;
        int best = Integer.MAX_VALUE;
        int kmax = Math.max(ra, rb);
        for (int k = 0; k <= kmax; k++) {
            int ra2 = Math.max(0, ra - k);
            int rb2 = Math.max(0, rb - k);
            int cost = k + ceilDiv(ra2, 3) + ceilDiv(rb2, 2);
            if (cost < best) best = cost;
        }
        return best;
    }
 
    // minimum total digits needed to reach required exponents (ra,rb,rc,rd)
    private static int minWork(int ra, int rb, int rc, int rd) {
        return rc + rd + minAB(ra, rb);
    }
 
    // build the lexicographically smallest zero-free string of length L
    // whose digit-product has exponents >= (ra,rb,rc,rd). Assumes feasibility
    // (minWork(ra,rb,rc,rd) <= L) has already been verified by the caller.
    private static String buildSuffix(int L, int ra, int rb, int rc, int rd) {
        int mw = minWork(ra, rb, rc, rd);
        int slack = L - mw;
        StringBuilder sb = new StringBuilder(L);
        for (int i = 0; i < slack; i++) sb.append('1');
 
        int remLen = mw;
        int cra = ra, crb = rb, crc = rc, crd = rd;
        while (remLen > 0) {
            for (int dg = 1; dg <= 9; dg++) {
                int nra = Math.max(0, cra - EXP2[dg]);
                int nrb = Math.max(0, crb - EXP3[dg]);
                int nrc = Math.max(0, crc - EXP5[dg]);
                int nrd = Math.max(0, crd - EXP7[dg]);
                int mw2 = minWork(nra, nrb, nrc, nrd);
                if (mw2 <= remLen - 1) {
                    sb.append((char) ('0' + dg));
                    cra = nra; crb = nrb; crc = nrc; crd = nrd;
                    remLen--;
                    break;
                }
            }
        }
        return sb.toString();
    }
 
    public String smallestNumber(String num, long t) {
        int a = 0, b = 0, c = 0, d = 0;
        while (t % 2 == 0) { a++; t /= 2; }
        while (t % 3 == 0) { b++; t /= 3; }
        while (t % 5 == 0) { c++; t /= 5; }
        while (t % 7 == 0) { d++; t /= 7; }
        if (t != 1) return "-1"; // t has a prime factor > 7, impossible
 
        int n = num.length();
        char[] digits = num.toCharArray();
 
        // 1) check num itself
        boolean zeroFree = true;
        int ta = 0, tb = 0, tc = 0, td = 0;
        for (char ch : digits) {
            int dv = ch - '0';
            if (dv == 0) zeroFree = false;
            ta += EXP2[dv]; tb += EXP3[dv]; tc += EXP5[dv]; td += EXP7[dv];
        }
        if (zeroFree && ta >= a && tb >= b && tc >= c && td >= d) {
            return num;
        }
 
        // 2) locate first zero (if any)
        int k = n;
        for (int i = 0; i < n; i++) {
            if (digits[i] == '0') { k = i; break; }
        }
 
        // prefix exponent sums for indices 0..k
        int[] pa = new int[k + 1];
        int[] pb = new int[k + 1];
        int[] pc = new int[k + 1];
        int[] pd = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            int dv = digits[i - 1] - '0';
            pa[i] = pa[i - 1] + EXP2[dv];
            pb[i] = pb[i - 1] + EXP3[dv];
            pc[i] = pc[i - 1] + EXP5[dv];
            pd[i] = pd[i - 1] + EXP7[dv];
        }
 
        // 3) try same-length answer: find rightmost position i0 (<= k) where we can
        // bump the digit above num[i0] and complete the suffix validly.
        int startI0 = Math.min(k, n - 1);
        String answer = null;
        for (int i0 = startI0; i0 >= 0 && answer == null; i0--) {
            int origDigit = digits[i0] - '0';
            int preA = pa[i0], preB = pb[i0], preC = pc[i0], preD = pd[i0];
            for (int dp = origDigit + 1; dp <= 9; dp++) {
                int newA = preA + EXP2[dp];
                int newB = preB + EXP3[dp];
                int newC = preC + EXP5[dp];
                int newD = preD + EXP7[dp];
                int remA = Math.max(0, a - newA);
                int remB = Math.max(0, b - newB);
                int remC = Math.max(0, c - newC);
                int remD = Math.max(0, d - newD);
                int L = n - 1 - i0;
                int mw = minWork(remA, remB, remC, remD);
                if (mw <= L) {
                    String suffix = buildSuffix(L, remA, remB, remC, remD);
                    StringBuilder sb = new StringBuilder(n);
                    sb.append(digits, 0, i0);
                    sb.append((char) ('0' + dp));
                    sb.append(suffix);
                    answer = sb.toString();
                    break;
                }
            }
        }
        if (answer != null) return answer;
 
        // 4) no same-length solution: build smallest number with more digits
        int mwFull = minWork(a, b, c, d);
        int Lp = Math.max(n + 1, mwFull);
        return buildSuffix(Lp, a, b, c, d);
    }
 
    // quick manual test
    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.smallestNumber("1234", 256));   // 1488
        System.out.println(s.smallestNumber("12355", 50));   // 12355
        System.out.println(s.smallestNumber("11111", 26));   // -1
    }
}