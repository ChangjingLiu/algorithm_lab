import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> LineSegmentList;
    private Point[] points;

    public BruteCollinearPoints(Point[] pointsIn)    // finds all line segments containing 4 points
    {
        //第一种异常
        if (pointsIn == null) throw new IllegalArgumentException("there is no point");
        //第二种异常
        int N = pointsIn.length;
        //二:if any point in the array is null
        for (int i = 0; i < N; i++) if (pointsIn[i] == null) throw new IllegalArgumentException("exist null point");
        points = new Point[N];
        for (int i = 0; i < N; i++) points[i] = pointsIn[i];
        Arrays.sort(points);
        //第三种异常
        for (int i = 1; i < N; i++)
            if (points[i - 1].compareTo(points[i]) == 0) throw new IllegalArgumentException("exist repeated point");


        LineSegmentList = new ArrayList<LineSegment>();
        for (int dot1 = 0; dot1 < N - 3; dot1++) {
            for (int dot2 = dot1 + 1; dot2 < N - 3; dot2++) {
                double k12 = points[dot2].slopeTo(points[dot1]);
                for (int dot3 = dot2 + 1; dot3 < N - 1; dot3++) {
                    double k13 = points[dot3].slopeTo(points[dot1]);
                    if (k12 != k13) continue;
                    for (int dot4 = dot3 + 1; dot4 < N; dot4++) {
                        double k14 = points[dot4].slopeTo(points[dot1]);
                        if (k14 != k12) continue;
                        //find the line
                        LineSegment linesegment = new LineSegment(points[dot1], points[dot4]);
                        LineSegmentList.add(linesegment);
                    }
                }
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return LineSegmentList.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segments = new LineSegment[LineSegmentList.size()];
        int index = 0;
        for (LineSegment Line : LineSegmentList) {
            segments[index++] = Line;
        }
        return segments;
    }

    public static void main(String[] args) {
        In in = new In("test/input8.txt");
        int n = in.readInt();
        StdOut.println("total " + n + " points");
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            StdOut.println("(" + x + "," + y + ")");
            points[i] = new Point(x, y);
        }
        //draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-22768, 52768);
        StdDraw.setYscale(-22768, 52768);

        StdDraw.setPenRadius(0.01);
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        StdDraw.setPenColor(StdDraw.RED);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.015);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    }
}
