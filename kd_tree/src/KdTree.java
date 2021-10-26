import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        //private int N;          // count
        private final int depth;
        //private boolean vert;

        public Node(Point2D p, int depth, RectHV rect) {
            if (p == null)
                throw new NullPointerException();
            this.p = p;
            //this.N = N;
            this.rect = rect;
            this.depth = depth;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else if (contains(p))
            return;
        else {
            RectHV Rect = new RectHV(0, 0, 1, 1);
            root = insert(root, p, 0, Rect);
            size++;
        }

    }

    private Node insert(Node x, Point2D p, int depth, RectHV rect) {
        if (x == null) {
            return new Node(p, depth, rect);//其他节点
        } else {
            //left,right
            if (depth % 2 == 0) {
                if (p.x() < x.p.x()) {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
                    x.lb = insert(x.lb, p, depth + 1, leftRect);
                } else {
                    RectHV rightRect = new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    x.rt = insert(x.rt, p, depth + 1, rightRect);
                }
            }
            //top,bottom
            else {
                if (p.y() < x.p.y()) {
                    RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
                    x.lb = insert(x.lb, p, depth + 1, bottomRect);
                } else {
                    RectHV topRect = new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
                    x.rt = insert(x.rt, p, depth + 1, topRect);
                }
            }
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else {
            if (root == null) {
                return false;
            } else {
                // 递归的写法
                return contains(p, root);
            }
        }
    }

    private int compare(Point2D p, Node n) {
        if (n.depth % 2 == 0) {
            // 如果是偶数层，按 x 比较
            if (Double.compare(p.x(), n.p.x()) == 0) {
                return Double.compare(p.y(), n.p.y());
            } else {
                return Double.compare(p.x(), n.p.x());
            }
        } else {
            // 按 y 比较
            if (Double.compare(p.y(), n.p.y()) == 0) {
                return Double.compare(p.x(), n.p.x());
            } else {
                return Double.compare(p.y(), n.p.y());
            }
        }
    }

    private boolean contains(Point2D p, Node node) {
        if (node == null) {
            return false;
        } else if (p.equals(node.p)) {
            return true;
        } else {
            if (compare(p, node) < 0) {
                return contains(p, node.lb);
            } else {
                return contains(p, node.rt);
            }
        }
    }

    public void draw() {
        for (Node node : Get_Nodes()) {


            if (node.depth % 2 == 0) {
                //vertical
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                //h
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
        }
        StdDraw.show();
    }

    private Iterable<Node> Get_Nodes() {
        Queue<Node> kNodes = new Queue<Node>();
        order(root, kNodes);
        return kNodes;
    }

    private void order(Node node, Queue<Node> q) {
        if (node == null) return;
        q.enqueue(node);
        order(node.lb, q);
        order(node.rt, q);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        SET<Point2D> inside_Nodes = new SET<Point2D>();

        range(root, inside_Nodes, rect);
        return inside_Nodes;
    }

    private void range(Node node, SET<Point2D> points, RectHV rect) {
        if (rect.contains(node.p)) {
            points.add(node.p);
        }
        if (node.lb != null && node.rect.intersects(rect)) {
            range(node.lb, points, rect);  //左子树
        }

        if (node.rt != null && node.rect.intersects(rect)) {
            range(node.rt, points, rect);  //右子树
        }

    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        if (root != null)
            return nearPoint(root, p, root).p;
        return null;
    }

    private Node nearPoint(Node kd, Point2D query, Node target) {
        if (kd == null) return target;
        double nrDist = query.distanceSquaredTo(target.p);//last find target distance
        double kdDist = query.distanceSquaredTo(kd.p);

        if (nrDist >= kdDist || nrDist >= kd.rect.distanceSquaredTo(query)) {

            if (nrDist > kdDist) target = kd;

            if (kd.depth % 2 == 0) {
                double cmpX = query.x() - kd.p.x();
                if (cmpX < 0.0) {
                    //左侧
                    if (kd.lb != null) target = nearPoint(kd.lb, query, target);
                    if (kd.rt != null) target = nearPoint(kd.rt, query, target);
                } else {
                    //右侧
                    if (kd.rt != null) target = nearPoint(kd.rt, query, target);
                    if (kd.lb != null) target = nearPoint(kd.lb, query, target);
                }
            } else {
                double cmpY = query.y() - kd.p.y();
                if (cmpY < 0.0) {
                    //下侧
                    if (kd.lb != null) target = nearPoint(kd.lb, query, target);
                    if (kd.rt != null) target = nearPoint(kd.rt, query, target);
                } else {
                    //上侧
                    if (kd.rt != null) target = nearPoint(kd.rt, query, target);
                    if (kd.lb != null) target = nearPoint(kd.lb, query, target);
                }
            }
        }
        return target;
    }

    public static void main(String[] args) {

    }
}
