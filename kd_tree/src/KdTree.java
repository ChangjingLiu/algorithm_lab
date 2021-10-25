import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;  // 根节点
    private int size; // 节点个数

    private class Node {
        Point2D point2d;  // 分割矩形的点
        RectHV rectHV;   // 分割矩形
        Node leftNode;   // 左子树节点
        Node rigthtNode;  // 右子树节点
        int depth;   // 节点的层数

        public Node(Point2D point2d, RectHV rectHV, int depth) {
            this.point2d = point2d;
            this.rectHV = rectHV;
            this.depth = depth;
        }
    }

    public KdTree()    // 构造一个空点集
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty()  // 这个集合是空的吗？
    {
        return size == 0;
    }

    public int size()  // 集合中的点数
    {
        return size;
    }

    private Node insert(Node insertPalceNode, Node perNode, Point2D thisPoint) {
        if (insertPalceNode == null) {
            if (size == 0)  // 原集合中无元素
                return new Node(thisPoint, new RectHV(0, 0, 1, 1), 1);

            else // 原集合中有元素，查找其父节点
            {
                int cmp = compare(perNode, thisPoint);
                RectHV rectHV = null;

                if (perNode.depth % 2 == 0)  // 父节点在偶数层，在上下侧插入
                {
                    if (cmp > 0) // 下方,同xmin，ymin，xmax；ymax = perNode.point.y
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.rectHV.ymin(),
                                perNode.rectHV.xmax(), perNode.point2d.y());

                    if (cmp < 0) // 上方,同xmax，ymax，xmin；ymin = perNode.point.y
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.point2d.y(),
                                perNode.rectHV.xmax(), perNode.rectHV.ymax());
                } else // 父节点在奇数层，在左右侧插入
                {
                    if (cmp > 0)  // 左侧， 同xmin，ymin，ymax；xmax = perNode.point.x
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.rectHV.ymin(),
                                perNode.point2d.x(), perNode.rectHV.ymax());

                    if (cmp < 0) // 右侧，同xmax，ymax，ymin；xmin = perNode.point.x
                        rectHV = new RectHV(perNode.point2d.x(), perNode.rectHV.ymin(),
                                perNode.rectHV.xmax(), perNode.rectHV.ymax());
                }
                return new Node(thisPoint, rectHV, perNode.depth + 1);
            }
        } else  // insertPalceNode != null
        {
            int cmp = compare(insertPalceNode, thisPoint);

            if (cmp > 0) // 下方或左侧，左子树
                insertPalceNode.leftNode = insert(insertPalceNode.leftNode, insertPalceNode, thisPoint);
            if (cmp < 0)  // 上方或右侧，右子树
                insertPalceNode.rigthtNode = insert(insertPalceNode.rigthtNode, insertPalceNode, thisPoint);
            return insertPalceNode;
        }
    }

    private int compare(Node pNode, Point2D thisPoint) {
        if (pNode == null)
            throw new java.lang.IllegalArgumentException("the Node object is null");
        if (thisPoint == null)
            throw new java.lang.IllegalArgumentException("the Point2D object is null");

        if (thisPoint.compareTo(pNode.point2d) == 0)
            return 0;

        if (pNode.depth % 2 != 0) // 父节点在奇数层，看放父节点的左右侧
        {
            if (Double.compare(pNode.point2d.x(), thisPoint.x()) == 1) // 小于0右侧
                return 1;
            else
                return -1;
        } else  // 父节点在偶数层，看放在父节点的上下侧
        {
            if (Double.compare(pNode.point2d.y(), thisPoint.y()) == 1) // 小于0上侧
                return 1;
            else
                return -1;
        }
    }

    public void insert(Point2D p)   // 将该点添加到集合中（如果它尚未在集合中）
    {
        if (p == null)
            throw new java.lang.IllegalArgumentException("the Point2D is null");

        if (contains(p))
            return;
        root = insert(root, null, p);
        size++;
    }

    private boolean containsP(Point2D p, Node cmpNoe) {
        if (cmpNoe == null)
            return false;
        int cmp = compare(cmpNoe, p);
        if (cmp > 0)  // 左子树
            return containsP(p, cmpNoe.leftNode);
        if (cmp < 0)  // 右子树
            return containsP(p, cmpNoe.rigthtNode);

        return true;


    }

    public boolean contains(Point2D p) // 集合是否包含点P？
    {
        if (p == null)
            throw new java.lang.IllegalArgumentException("the Point2D is null");
        return containsP(p, root);
    }


    public void draw() // 把所有点画成标准画
    {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        draw(x.leftNode);
        draw(x.rigthtNode);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point2d.draw();
        StdDraw.setPenRadius();
        // draw the splitting line segment
        if (x.depth % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point2d.x(), x.rectHV.ymin(), x.point2d.x(), x.rectHV.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rectHV.xmin(), x.point2d.y(), x.rectHV.xmax(), x.point2d.y());
        }
    }

    private void range(Node currentNode, Queue<Point2D> queue, RectHV rectHV) {
        if (rectHV.contains(currentNode.point2d)) // 矩形中包含点
            queue.enqueue(currentNode.point2d);

        if (currentNode.leftNode != null && currentNode.leftNode.rectHV.intersects(rectHV)) {
            range(currentNode.leftNode, queue, rectHV);  //左子树
        }

        if (currentNode.rigthtNode != null && currentNode.rigthtNode.rectHV.intersects(rectHV)) {
            range(currentNode.rigthtNode, queue, rectHV); // 右子树
        }

    }

    public Iterable<Point2D> range(RectHV rect)  // 在矩形（或边界）内的所有点
    {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("The RectHV is null");

        Queue<Point2D> queue = new Queue<>();  //  队列用于存储在矩形内（包含边界）的点
        if (root == null) {
            return queue;
        }
        range(root, queue, rect);   //从根节点开始遍历

        return queue;
    }


    private Node nearest(Node currentNode, Node nearestNode, Point2D p) {
        if (currentNode == null)
            return nearestNode;

        double nearstDistance = Double.POSITIVE_INFINITY;   // 当前最短距离
        double currentDistance = p.distanceSquaredTo(currentNode.point2d); // 当前节点距离

        if (nearestNode != null) {  // 根据当前最近节点计算最短距离
            nearstDistance = p.distanceSquaredTo(nearestNode.point2d);
        } else {  // 无当前最近节点，当前节点即为最近节点
            nearstDistance = currentDistance;
            nearestNode = currentNode;
        }

        if (currentDistance < nearstDistance)  // 更改最近节点信息
        {
            nearestNode = currentNode;
            nearstDistance = currentDistance;
        }

        int cmp = compare(currentNode, p);

        if (cmp > 0)  // 点位于当前节点的左子树
        {
            nearestNode = nearest(currentNode.leftNode, nearestNode, p);

            // p点距离该节点水平线的垂直距离小于当前最短距离，在当前点的上侧才有机会存在最近的点
            if (currentNode.rigthtNode != null &&
                    currentNode.rigthtNode.rectHV.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point2d))
                nearestNode = nearest(currentNode.rigthtNode, nearestNode, p);
        } else if (cmp < 0)  // 点位于当前节点的右子树
        {
            nearestNode = nearest(currentNode.rigthtNode, nearestNode, p);

            // p点距离该节点水平线的垂直距离小于当前最短距离，在当前点的下侧才有机会存在最近的点
            if (currentNode.leftNode != null &&
                    currentNode.leftNode.rectHV.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point2d))
                nearestNode = nearest(currentNode.leftNode, nearestNode, p);
        }
        return nearestNode;
    }

    public Point2D nearest(Point2D p) // 集合为点p的最近邻；如果集合为空，则为null。
    {
        if (root == null)
            return null;

        return nearest(root, null, p).point2d;
    }


    public static void main(String[] args)  // 单元测试的方法（可选）
    {
        KdTree kdTree = new KdTree();
        Point2D[] point2ds = new Point2D[5];
        point2ds[0] = new Point2D(0.7, 0.2);
        point2ds[1] = new Point2D(0.5, 0.4);
        point2ds[2] = new Point2D(0.2, 0.3);
        point2ds[3] = new Point2D(0.4, 0.7);
        point2ds[4] = new Point2D(0.9, 0.6);

        for (int i = 0; i < point2ds.length; i++) {
            System.out.println("*************i=" + i + "***************");
            kdTree.insert(point2ds[i]);
            System.out.println(kdTree.size());
        }

        System.out.println(kdTree.contains(point2ds[4]));

        Iterable<Point2D> iterable = kdTree.range(new RectHV(0, 0, 1, 1));

        for (Point2D point2d : iterable) {
            System.out.println(point2d.toString());
        }
        System.out.println();
        System.out.println(kdTree.root.point2d.toString());
        System.out.println(kdTree.root.leftNode.point2d.toString());
        System.out.println(kdTree.root.rigthtNode.point2d.toString());
        System.out.println(kdTree.root.leftNode.leftNode.point2d.toString());
        System.out.println(kdTree.root.leftNode.rigthtNode.point2d.toString());

        System.out.println(kdTree.size);
        System.out.println(kdTree.nearest(new Point2D(0.111, 0.494)));
    }
}
