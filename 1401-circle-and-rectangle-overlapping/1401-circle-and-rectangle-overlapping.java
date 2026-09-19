class Solution {
    public boolean checkOverlap(int radius, int xCenter, int yCenter,
                                int x1, int y1, int x2, int y2) {

        // Find the closest point in the rectangle to the circle's center
        int closestX = Math.max(x1, Math.min(xCenter, x2));
        int closestY = Math.max(y1, Math.min(yCenter, y2));

        // Check whether the distance to that point is <= radius
        long dx = closestX - xCenter;
        long dy = closestY - yCenter;

        return dx * dx + dy * dy <= (long) radius * radius;
    }
}
