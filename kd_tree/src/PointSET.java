import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> points;

    public PointSET()    // 构造一个空点集
    {
        points = new SET<Point2D>();
    }

    public boolean isEmpty()  // 这个集合是空的吗？
    {
        return points.isEmpty();
    }

    public int size()  // 集合中的点数
    {
        return points.size();
    }

    public void insert(Point2D p)   // 将该点添加到集合中（如果它尚未在集合中）
    {
        if (p == null)
            throw new java.lang.IllegalArgumentException("this Point2D is null");

        points.add(p);
    }

    public boolean contains(Point2D p) // 集合是否包含点P？
    {
        if (p == null)
            throw new java.lang.IllegalArgumentException("this Point2D is null");

        return points.contains(p);
    }

    public void draw()  // 把所有点画成标准画
    {
        for (Point2D point2d : points) {
            point2d.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)  // 在矩形（或边界）内的所有点
    {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("The RectHV is null");

        Queue<Point2D> queue = new Queue<>();  //  队列用于存储在矩形内（包含边界）的点

        for (Point2D point2d : points) {
            if (rect.contains(point2d))
                queue.enqueue(point2d);  // 进队列
        }

        return queue;
    }

    public Point2D nearest(Point2D p) // 集合为点p的最近邻；如果集合为空，则为null。
    {
        if (points == null) // 集合为空
            return null;

        Point2D point2dNearest = null;
        double distanceMin = Double.POSITIVE_INFINITY;  // 两点间欧式距离平方

        for (Point2D point2d : points) {
            double distanceCurrent = point2d.distanceSquaredTo(p);
            if (distanceCurrent < distanceMin)  // 遍历找到距离最小的点
            {
                point2dNearest = point2d;
                distanceMin = distanceCurrent;
            }
        }

        return point2dNearest;
    }

    public static void main(String[] args)  // 单元测试的方法（可选）
    {
        System.out.println(Double.compare(0.2, 0.3));
        PointSET pointSET = new PointSET();

        Point2D[] point2ds = new Point2D[8];
        for (int i = 0; i < point2ds.length; i++) {
            point2ds[i] = new Point2D(i / 10.0, (i + 1) / 10.0);
            pointSET.insert(point2ds[i]);
        }

        System.out.println(pointSET.size());

        System.out.println(pointSET.contains(new Point2D(0.3, 0.3)));
        System.out.println(pointSET.nearest(new Point2D(0.3, 0.6)));
        RectHV rectHV = new RectHV(0.2, 0.2, 0.6, 0.9);
        Iterable<Point2D> pQueue = pointSET.range(rectHV);

        for (Point2D point2d : pQueue) {
            System.out.println(point2d);
        }

    }
}
