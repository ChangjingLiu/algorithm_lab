import edu.princeton.cs.algs4.*;

public class KdTree {
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int N;          // count
        private int depth;
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
        return size(root) == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    public void insert(Point2D p) {
        RectHV Rect = new RectHV(0, 0, 1, 1);
        root = insert(root, p, 0, Rect);
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

    public Iterable<Node> Get_Nodes() {
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
        for (Node i : Get_Nodes()) {
            if (rect.contains(i.p)) {
                inside_Nodes.add(i.p);
            }
        }
        //SET<Point2D> inside_Points = new SET<Point2D>();


        return inside_Nodes;
    }

    public static void main(String[] args) {

    }
}
