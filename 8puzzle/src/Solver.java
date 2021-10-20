import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private TreeNode currentNode;
    private TreeNode currenttwinNode;
    private Stack<Board> stackBoard;

    // find a solution to the initial board (using the A* algorithm)
    private class TreeNode implements Comparable<TreeNode> {
        private final Board board;
        private final TreeNode pre;
        private final int moves;
        private final int priority;

        private TreeNode(Board b, TreeNode pre) {
            this.board = b;
            this.pre = pre;
            if (pre != null) {
                this.moves = pre.moves + 1;
                this.priority = this.moves + this.board.manhattan();
            } else {
                //pre==null
                moves = 0;
                this.priority = this.moves + this.board.manhattan();
            }
        }

        public TreeNode getPre() {
            return pre;
        }

        public int getMoves() {
            return moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getPriority() {
            return priority;
        }

        public int compareTo(TreeNode a) {
            if (this.priority == a.priority) {
                return this.board.manhattan() - a.board.manhattan();
            }
            return this.priority - a.priority;
        }
    }

    private final boolean solvable;

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (isSolvable())
            return currentNode.getMoves();
        else
            return -1;
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();

        currentNode = new TreeNode(initial, null);
        MinPQ<TreeNode> PQ = new MinPQ<>();
        PQ.insert(currentNode);

        currenttwinNode = new TreeNode(initial.twin(), null);
        MinPQ<TreeNode> twinPQ = new MinPQ<>();
        twinPQ.insert(currenttwinNode);
        //boolean flag = false;
        while (true) {
            //origin
            currentNode = PQ.delMin();
            if (currentNode.getBoard().isGoal()) {
                solvable = true;
                break;
                //found
            } else {
                for (Board it : currentNode.getBoard().neighbors()) {
                    //==null 针对第一个Node
                    if (currentNode.getPre() == null ||
                            !it.equals(currentNode.getPre().getBoard())) {
                        PQ.insert(new TreeNode(it, currentNode));
                    }
                }
            }

            //twin tiles
            currenttwinNode = twinPQ.delMin();
            if (currenttwinNode.getBoard().isGoal()) {
                solvable = false;
                break;
                //found
            } else {
                for (Board it : currenttwinNode.getBoard().neighbors()) {
                    //==null 针对第一个Node
                    if (currenttwinNode.getPre() == null ||
                            !it.equals(currenttwinNode.getPre().getBoard())) {
                        twinPQ.insert(new TreeNode(it, currenttwinNode));
                    }
                }
            }
        }

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        stackBoard = new Stack<>();
        TreeNode nowNode = currentNode;
        while (nowNode != null) {
            stackBoard.push(nowNode.getBoard());
            nowNode = nowNode.getPre();
        }
        return stackBoard;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        int[][] blocks = new int[3][3];

        blocks[0][0] = 1;
        blocks[0][1] = 2;
        blocks[0][2] = 3;

        blocks[1][0] = 4;
        blocks[1][1] = 5;
        blocks[1][2] = 6;

        blocks[2][0] = 8;
        blocks[2][1] = 7;
        blocks[2][2] = 0;
        Board board = new Board(blocks);

        Solver solver = new Solver(board);
        //System.out.println(solver.currentNode.getPre() == null);
        //System.out.println(solver.currentNode.getPre());
        if (!solver.isSolvable()) {
            System.out.println("this board is can't resolve");
        } else {
            Iterable<Board> bIterable = solver.solution();
            //System.out.println(bIterable.toString());
            //System.out.println("444");
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board it : bIterable) {

                System.out.println(it.toString());
            }
        }


    }
}
