class Solution {
    public int[] nodesBetweenCriticalPoints(ListNode head) {
        int minDistance = Integer.MAX_VALUE;
        int maxDistance = -1;

        int firstCritical = -1;
        int prevCritical = -1;

        ListNode prev = head;
        ListNode curr = head.next;

        int index = 1;

        while (curr.next != null) {
            ListNode next = curr.next;

            // Check if curr is a local maxima or local minima
            boolean isCritical =
                (curr.val > prev.val && curr.val > next.val) ||
                (curr.val < prev.val && curr.val < next.val);

            if (isCritical) {
                if (firstCritical == -1) {
                    // First critical point
                    firstCritical = index;
                } else {
                    // Distance from previous critical point
                    minDistance = Math.min(minDistance, index - prevCritical);

                    // Distance from first critical point
                    maxDistance = index - firstCritical;
                }

                prevCritical = index;
            }

            prev = curr;
            curr = next;
            index++;
        }

        if (maxDistance == -1) {
            return new int[]{-1, -1};
        }

        return new int[]{minDistance, maxDistance};
    }
}
