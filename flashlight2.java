
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class flashlight2 {
    private int targetX;
    private int targetY;
    private int x;
    private int y;

    private ArrayList<Rectangle> rects;

    public flashlight2(ArrayList<Rectangle> rects) {
        this.rects = rects;
        x = 100;
        y = 100;
    }

    public void update() {

    }

    public void draw(Graphics2D g2d) {

        ArrayList intersections = new ArrayList();
        Line l1 = new Line(x, y, targetX, targetY);
        Line l2 = new Line(200, 100, 200, 500);
        g2d.drawLine((int) l2.getStartX(), (int) l2.getStartY(), (int) l2.getEndX(), (int) l2.getEndY());

        Point l1Start = new Point((int) l1.getStartX(), (int) l1.getStartY());
        Point l2Start = new Point((int) l2.getStartX(), (int) l2.getStartY());
        Point l1End = new Point((int) l1.getEndX(), (int) l1.getEndY());
        Point l2End = new Point((int) l2.getEndX(), (int) l2.getEndY());

        if (doIntersect(l1Start, l1End, l2Start, l2End) != null)
            g2d.drawLine(x, y, doIntersect(l1Start, l1End, l2Start, l2End).x, doIntersect(l1Start, l1End, l2Start, l2End).y);
        else
            g2d.drawLine(x, y, x+  (int)(200 * Math.cos(Math.toRadians(getAngle(new Point(targetX, targetY))))), y +(int)(200 *Math.sin(Math.toRadians(getAngle(new Point(targetX, targetY))))));
    }


    public double getAngle(Point p) {
        double angle = Math.toDegrees(Math.atan2(p.getY() - y, p.getX() - x));
        return angle;

    }

    public boolean onSegment(Point p, Point q, Point r) {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
            return true;

        return false;
    }

    public float orientation(Point p, Point q, Point r) {
        // https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
        float val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public Point doIntersect(Point p1, Point q1, Point p2, Point q2) {
        // Find the four orientations needed for general and
        // special cases
        float o1 = orientation(p1, q1, p2);
        float o2 = orientation(p1, q1, q2);
        float o3 = orientation(p2, q2, p1);
        float o4 = orientation(p2, q2, q1);

        double a1 = q1.y - p1.y;
        double b1 = p1.x - q1.x;
        double c1 = a1 * (p1.x) + b1 * (p1.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = q2.y - p2.y;
        double b2 = p2.x - q2.x;
        double c2 = a2 * (p2.x) + b2 * (p2.y);

        double determinant = a1 * b2 - a2 * b1;

        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;

        Point point = new Point((int) x, (int) y);

        // General case
        if (o1 != o2 && o3 != o4)
            return point;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return point;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return point;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return point;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return point;

        return null; // Doesn't fall in any of the above cases
    }


    public Point lineLineIntersection(Point A, Point B, Point C, Point D) {
        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1 * (A.x) + b1 * (A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2 * (C.x) + b2 * (C.y);

        double determinant = a1 * b2 - a2 * b1;

        int la1 = (B.y - A.y) / (B.x - A.x);
        int lb1 = A.y - la1 * A.x;

        int la2 = (D.y - C.y) / (D.x - C.x);
        int lb2 = C.y - la2 * C.x;

        int x0 = -(lb1 - lb2) / (la1 - la2);

        if (x0 > Math.min(A.x, B.x) && x0 < Math.max(A.x, B.x) &&
                x0 > Math.min(C.x, D.x) && x0 < Math.max(C.x, D.x)) {
            System.out.println("=");
        }

        if (determinant == 0) {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return null;
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Point((int) x, (int) y);
        }
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}
