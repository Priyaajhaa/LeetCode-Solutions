class Solution {
    static class Node {
        int leftChar, rightChar;
        int prefixLen, suffixLen, maxLen;
        int len;

        Node() {}

        Node(char c) {
            leftChar = rightChar = c - 'a';
            prefixLen = suffixLen = maxLen = len = 1;
        }
    }

    private Node[] tree;
    private char[] s;

    public int[] longestRepeating(String s, String queryCharacters, int[] queryIndices) {
        int n = s.length();
        int k = queryCharacters.length();

        this.s = s.toCharArray();
        tree = new Node[4 * n];

        build(1, 0, n - 1);

        int[] ans = new int[k];

        for (int i = 0; i < k; i++) {
            int index = queryIndices[i];
            char c = queryCharacters.charAt(i);

            this.s[index] = c;
            update(1, 0, n - 1, index, c);

            ans[i] = tree[1].maxLen;
        }

        return ans;
    }

    private void build(int node, int l, int r) {
        if (l == r) {
            tree[node] = new Node(s[l]);
            return;
        }

        int mid = l + (r - l) / 2;

        build(node * 2, l, mid);
        build(node * 2 + 1, mid + 1, r);

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    private void update(int node, int l, int r, int index, char c) {
        if (l == r) {
            tree[node] = new Node(c);
            return;
        }

        int mid = l + (r - l) / 2;

        if (index <= mid) {
            update(node * 2, l, mid, index, c);
        } else {
            update(node * 2 + 1, mid + 1, r, index, c);
        }

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    private Node merge(Node a, Node b) {
        Node res = new Node();

        res.len = a.len + b.len;
        res.leftChar = a.leftChar;
        res.rightChar = b.rightChar;

        // Prefix
        res.prefixLen = a.prefixLen;
        if (a.prefixLen == a.len && a.rightChar == b.leftChar) {
            res.prefixLen = a.len + b.prefixLen;
        }

        // Suffix
        res.suffixLen = b.suffixLen;
        if (b.suffixLen == b.len && a.rightChar == b.leftChar) {
            res.suffixLen = b.len + a.suffixLen;
        }

        // Best answer entirely inside one child
        res.maxLen = Math.max(a.maxLen, b.maxLen);

        // Best answer crossing the boundary
        if (a.rightChar == b.leftChar) {
            res.maxLen = Math.max(
                res.maxLen,
                a.suffixLen + b.prefixLen
            );
        }

        return res;
    }
}