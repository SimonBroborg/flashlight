import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FlashLight {
    private ArrayList<int[]> segments;
    private int[] xPoints;
    private int[] yPoints;

    private ArrayList<Rectangle> rects;


    private ArrayList<Map> intersections;

    private int x;
    private int y;
    private int targetX;
    private int targetY;

    public FlashLight(ArrayList<Rectangle> rects) {
        this.rects = rects;
        segments = new ArrayList<>();
        intersections = new ArrayList<>();
        x = 320;
        y = 240;
        targetX = 0;
        targetY = 0;
    }

    public void update() {

    }

    public void draw(Graphics2D g2d) {
        intersections = new ArrayList<>();
        segments = new ArrayList<>();
        g2d.setColor(Color.red);
        for (Rectangle rect : rects) {
            g2d.fill(rect);
            // Only left
            if (x < rect.x && y < rect.y + rect.height && y > rect.y) {
                segments.add(new int[]{rect.x, rect.y + rect.height, rect.x, rect.y});
                // Only right
            } else if (x > rect.x + rect.width && y < rect.y + rect.height && y > rect.y) {
                segments.add(new int[]{rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height});
                // Only top
            } else if (y < rect.y && x > rect.x && x < rect.x + rect.width) {
                segments.add(new int[]{rect.x, rect.y, rect.x + rect.width, rect.y});
                // Only bottom
            } else if (y > rect.y + rect.height && x > rect.x && x < rect.x + rect.width) {
                segments.add(new int[]{rect.x + rect.width, rect.y + rect.height, rect.x, rect.y + rect.height});
                // Upper left
            } else if (x < rect.x + rect.width && y < rect.y + rect.height) {
                segments.add(new int[]{rect.x, rect.y, rect.x + rect.width, rect.y});
                segments.add(new int[]{rect.x, rect.y + rect.height, rect.x, rect.y});
            }
            // upper right
            else if (x > rect.x && y < rect.y + rect.height) {
                segments.add(new int[]{rect.x, rect.y, rect.x + rect.width, rect.y});
                segments.add(new int[]{rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height});
            }
            // bottom right
            else if (x > rect.x && y > rect.y) {
                segments.add(new int[]{rect.x + rect.width, rect.y + rect.height, rect.x, rect.y + rect.height});
                segments.add(new int[]{rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height});
            }
            // bottom left
            else if (x < rect.x + rect.width && y > rect.y) {
                segments.add(new int[]{rect.x + rect.width, rect.y + rect.height, rect.x, rect.y + rect.height});
                segments.add(new int[]{rect.x, rect.y + rect.height, rect.x, rect.y});
            } else {
                // top
                segments.add(new int[]{rect.x, rect.y, rect.x + rect.width, rect.y});
                // right side
                segments.add(new int[]{rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height});
                // bottom
                segments.add(new int[]{rect.x + rect.width, rect.y + rect.height, rect.x, rect.y + rect.height});
                // left side
                segments.add(new int[]{rect.x, rect.y + rect.height, rect.x, rect.y});
            }
        }
        // The border
        segments.add(new int[]{0, 0, 640, 0});
        segments.add(new int[]{640, 0, 640, 480});
        segments.add(new int[]{640, 480, 0, 480});
        segments.add(new int[]{0, 480, 0, 0});


        ArrayList<int[]> points = new ArrayList<>();
        for (int[] segment : segments) {
            points.add(new int[]{segment[0], segment[1]});
            points.add(new int[]{segment[2], segment[3]});
        }

        ArrayList<Float> uniAngles = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            int[] uniquePoint = points.get(i);
            double angle = Math.atan2(uniquePoint[1] - targetY, uniquePoint[0] - targetX);

            uniAngles.add((float) angle);
            uniAngles.add((float) (angle - 0.00001));
            uniAngles.add((float) (angle + 0.0001));

        }

        for (Float uniAngle : uniAngles) {
            float angle = uniAngle;
            // Calculate dx & dy from angle
            double dx = Math.toDegrees(Math.cos(angle));
            double dy = Math.toDegrees(Math.sin(angle));


            // Find closest intersection
            Map closestIntersection = null;
            for (int[] segment : segments) {
                //g2d.drawLine(segment[0], segment[1], segment[2], segment[3]);
                Map intersect = getIntersection(targetX + dx, targetY + dy, segment);
                if (intersect == null) {
                    continue;
                }
                if (closestIntersection == null || (int) intersect.get("param") < (int) closestIntersection.get("param")) {
                    closestIntersection = intersect;
                }
            }
            closestIntersection.put("angle", angle);
            intersections.add(closestIntersection);
        }

        Collections.sort(intersections, new MapComparator());

        xPoints = new int[intersections.size()];
        yPoints = new int[intersections.size()];


        for (int i = 0; i < intersections.size(); i++) {
            xPoints[i] = (int) intersections.get(i).get("x");
            yPoints[i] = (int) intersections.get(i).get("y");
        }
        Point2D center;
        float[] dist;
        Color[] colors;

        RadialGradientPaint p;
        float radius = 200;

        dist = new float[]{0.0f, 1.0f};
        colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black};

        center = new Point2D.Float(x, y);

        //p = new RadialGradientPaint(center, radius, dist, colors);
        //g2d.setPaint(p);
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));

        //g2d.fillPolygon(xPoints, yPoints, intersections.size());

        Polygon poly = new Polygon();
        poly.npoints = intersections.size();
        poly.xpoints = xPoints;
        poly.ypoints = yPoints;
        Area outer = new Area(new Rectangle(0, 0, 640, 480));
        outer.subtract(new Area(poly));



        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.setColor(Color.BLACK);
        g2d.fill(outer);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        for (Map intersect : intersections) {
            g2d.drawLine(x, y, (int) intersect.get("x"), (int) intersect.get("y"));
            //g2d.fillOval((int) intersect.get("x") - 5, (int) intersect.get("y") - 5, 10, 10);
        }
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getAngle(Point p) {
        double angle = Math.toDegrees(Math.atan2(p.getY() - y, p.getX() - x));
        return angle;

    }

    public Map<String, Integer> getIntersection(double tx, double ty, int[] segment) {

        // ray in parametric: Point + Direction * T1
        float r_px = x;
        float r_py = y;
        double r_dx = tx - x;
        double r_dy = ty - y;

        // segment in parametric: Point + Direction * T1
        float s_px = segment[0];
        float s_py = segment[1];
        float s_dx = segment[2] - segment[0];
        float s_dy = segment[3] - segment[1];

        double r_mag = Math.sqrt(r_dx * r_dx + r_dy * r_dy);
        double s_mag = Math.sqrt(s_dx * s_dx + s_dy * s_dy);
        if (r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag) {
            // Unit vectors are the same.
            return null;
        }

        double T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        double T1 = (s_px + s_dx * T2 - r_px) / r_dx;

        // Must be within parametric whatevers for RAY/SEGMENT
        if (T1 < 0) return null;
        if (T2 < 0 || T2 > 1) return null;

        Map<String, Integer> map = new HashMap<>();
        map.put("x", (int) (r_px + r_dx * T1));
        map.put("y", (int) (r_py + r_dy * T1));
        map.put("param", (int) T1);

        return map;
    }
}