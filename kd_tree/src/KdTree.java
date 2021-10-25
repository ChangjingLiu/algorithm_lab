import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

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

        public Node(Point2D p, int depth) {
            if (p == null)
                throw new NullPointerException();
            this.p = p;
            //this.N = N;
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
        root = insert(root, p, 0);
    }

    private Node insert(Node x, Point2D p, int depth) {
        if (x == null) {
            return new Node(p, depth);//其他节点
        } else {
            //left,right
            if (depth % 2 != 0) {
                if (p.x() < x.p.x()) {
                    x.lb = insert(x.lb, p, depth + 1);
                } else {
                    x.rt = insert(x.rt, p, depth + 1);
                }
            }
            //top,bottom
            else {
                if (p.y() < x.p.y()) {
                    x.lb = insert(x.lb, p, depth + 1);
                } else {
                    x.rt = insert(x.rt, p, depth + 1);
                }
            }
        }
        return x;
    }

    public void draw() {
        for (Node node : Allnode()) {
            node.p.draw();
            if (node.depth % 2 != 0) {
                //vertical
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), 0, node.p.x(), 1);
            } else {
                //h
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(0, node.p.y(), 1, node.p.y());
            }
        }
        StdDraw.show();
    }

    public Iterable<Node> Allnode() {
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

    public static void main(String[] args) {

    }
}
