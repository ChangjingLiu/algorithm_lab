import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final SET<Point2D> Points;

    public PointSET() {
        Points = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return Points.isEmpty();
    }

    public int size() {
        return Points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!Points.contains(p)) {
            Points.add(p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return Points.contains(p);
    }

    public void draw() {
        for (Point2D p : Points) {
            p.draw();
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        SET<Point2D> inside_Points = new SET<Point2D>();
        for (Point2D p : Points) {
            if (rect.contains(p)) {
                inside_Points.add(p);
            }
        }
        return inside_Points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D point = null;
        double min_dis = Double.MAX_VALUE;
        for (Point2D p1 : Points) {
            double dis = p.distanceSquaredTo(p1);
            if (dis < min_dis) {
                point = p1;
                min_dis = dis;
            }
        }
        return point;
    }

    public static void main(String[] args) {

    }
}
