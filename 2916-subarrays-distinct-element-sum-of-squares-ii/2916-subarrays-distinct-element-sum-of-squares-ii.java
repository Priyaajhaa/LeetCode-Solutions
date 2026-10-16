class Solution {
    static final int MOD = 1_000_000_007;

    public int sumCounts(int[] nums) {
        int n = nums.length;
        SegTree seg = new SegTree(n);

        int[] last = new int[100001];
        long ans = 0;

        for (int i = 1; i <= n; i++) {
            int x = nums[i - 1];

            int prev = last[x];
            last[x] = i;

            // Increase distinct counts for subarrays
            // ending at i by 1 for start positions (prev, i].
            seg.update(prev + 1, i, 1);

            // Sum of squares of distinct counts for all
            // subarrays ending at i.
            ans = (ans + seg.query(1, i)) % MOD;
        }

        return (int) ans;
    }

    class SegTree {
        int n;
        long[] sum;
        long[] sumSq;
        long[] lazy;

        SegTree(int n) {
            this.n = n;
            sum = new long[4 * n + 5];
            sumSq = new long[4 * n + 5];
            lazy = new long[4 * n + 5];
        }

        void apply(int node, int l, int r, long val) {
            long len = r - l + 1L;

            // (x + val)^2 = x^2 + 2*val*x + val^2
            sumSq[node] = (
                sumSq[node]
                + 2L * val % MOD * sum[node] % MOD
                + len % MOD * val % MOD * val % MOD
            ) % MOD;

            sum[node] = (sum[node] + len * val) % MOD;
            lazy[node] = (lazy[node] + val) % MOD;
        }

        void push(int node, int l, int r) {
            if (lazy[node] == 0 || l == r) {
                return;
            }

            int mid = (l + r) / 2;
            long val = lazy[node];

            apply(node * 2, l, mid, val);
            apply(node * 2 + 1, mid + 1, r, val);

            lazy[node] = 0;
        }

        void update(int ql, int qr, int val) {
            update(1, 1, n, ql, qr, val);
        }

        void update(int node, int l, int r,
                    int ql, int qr, int val) {
            if (ql > r || qr < l) {
                return;
            }

            if (ql <= l && r <= qr) {
                apply(node, l, r, val);
                return;
            }

            push(node, l, r);

            int mid = (l + r) / 2;

            update(node * 2, l, mid, ql, qr, val);
            update(node * 2 + 1, mid + 1, r, ql, qr, val);

            sum[node] = (sum[node * 2] + sum[node * 2 + 1]) % MOD;
            sumSq[node] = (sumSq[node * 2] + sumSq[node * 2 + 1]) % MOD;
        }

        long query(int ql, int qr) {
            return query(1, 1, n, ql, qr);
        }

        long query(int node, int l, int r,
                   int ql, int qr) {
            if (ql > r || qr < l) {
                return 0;
            }

            if (ql <= l && r <= qr) {
                return sumSq[node];
            }

            push(node, l, r);

            int mid = (l + r) / 2;

            return (
                query(node * 2, l, mid, ql, qr)
                + query(node * 2 + 1, mid + 1, r, ql, qr)
            ) % MOD;
        }
    }
}