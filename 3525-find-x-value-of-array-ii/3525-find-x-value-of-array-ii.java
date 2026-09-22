class Solution {

    class Node {
        int prod;
        int[] cnt;

        Node() {
            cnt = new int[k];
        }
    }

    private int k;
    private Node[] tree;

    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.k = k;

        int n = nums.length;
        tree = new Node[n * 4];

        build(1, 0, n - 1, nums);

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int idx = queries[i][0];
            int value = queries[i][1];
            int start = queries[i][2];
            int x = queries[i][3];

            update(1, 0, n - 1, idx, value);

            Node res = query(1, 0, n - 1, start, n - 1);

            ans[i] = res.cnt[x];
        }

        return ans;
    }

    private Node merge(Node left, Node right) {
        Node res = new Node();

        res.prod = (left.prod * right.prod) % k;

        for (int i = 0; i < k; i++) {
            res.cnt[i] = left.cnt[i];
        }

        for (int i = 0; i < k; i++) {
            res.cnt[(i * left.prod) % k] += right.cnt[i];
        }

        return res;
    }

    private void build(int node, int l, int r, int[] nums) {
        tree[node] = new Node();

        if (l == r) {
            int v = nums[l] % k;
            tree[node].prod = v;
            tree[node].cnt[v] = 1;
            return;
        }

        int mid = (l + r) >> 1;

        build(node << 1, l, mid, nums);
        build(node << 1 | 1, mid + 1, r, nums);

        tree[node] = merge(tree[node << 1], tree[node << 1 | 1]);
    }

    private void update(int node, int l, int r, int idx, int value) {
        if (l == r) {
            int v = value % k;

            tree[node].prod = v;

            for (int i = 0; i < k; i++) {
                tree[node].cnt[i] = 0;
            }

            tree[node].cnt[v] = 1;
            return;
        }

        int mid = (l + r) >> 1;

        if (idx <= mid) {
            update(node << 1, l, mid, idx, value);
        } else {
            update(node << 1 | 1, mid + 1, r, idx, value);
        }

        tree[node] = merge(tree[node << 1], tree[node << 1 | 1]);
    }

    private Node query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }

        int mid = (l + r) >> 1;

        if (qr <= mid) {
            return query(node << 1, l, mid, ql, qr);
        }

        if (ql > mid) {
            return query(node << 1 | 1, mid + 1, r, ql, qr);
        }

        return merge(
                query(node << 1, l, mid, ql, qr),
                query(node << 1 | 1, mid + 1, r, ql, qr)
        );
    }
}