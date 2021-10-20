import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> LineSegmentList;
    private Point[] points;

    public FastCollinearPoints(Point[] pointsIn)     // finds all line segments containing 4 or more points
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
        //exception
        // current point p
        LineSegmentList = new ArrayList<LineSegment>();

        Point currentpoint;
        Point[] otherPoints = new Point[N - 1];

        for (int i = 0; i < N; i++) {
            //current point
            currentpoint = points[i];
            //For each other point q, determine the slope it makes with p.
            for (int j = 0; j < N; j++) {
                if (j > i) {
                    otherPoints[j - 1] = points[j];
                }
                if (j < i) {
                    otherPoints[j] = points[j];
                }
            }
            //sort with slope
            Arrays.sort(otherPoints, currentpoint.slopeOrder());
            //System.out.println("sorted");
            for (Point p : otherPoints) {
                //System.out.println(p);
            }

            //check other 3 points
            int count = 2;
            Point start = points[i];
            Point end = null;
            for (int k = 1; k < N - 1; k++) {
                Point prepoint = otherPoints[k - 1];
                Point nextpoint = otherPoints[k];
                if (prepoint.slopeTo(currentpoint) == nextpoint.slopeTo(currentpoint)) {
                    count++;
                    if (k == N - 2) {
                        if (count >= 4 && start.compareTo(otherPoints[k - count + 2]) < 0) {
                            end = nextpoint;
                            LineSegmentList.add(new LineSegment(start, end));
                        }
                    }

                } else {
                    if (start.compareTo(prepoint) < 0 && start.compareTo(otherPoints[k - count + 1]) < 0) {
                        if (count >= 4) {
                            end = prepoint;
                            LineSegmentList.add(new LineSegment(start, end));
                        }
                    }
                    //重新计数
                    count = 2;
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
        In in = new In("test/input10000.txt");
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
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
