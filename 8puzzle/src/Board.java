import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        //创建(复制)一个
        this.N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString() {
        //StringBuilder 用于修改字符串
        StringBuilder s = new StringBuilder();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(" " + tiles[i][j]);
            }
            s.append("\n");
        }
        String string = s.toString();
        return string;
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int k = 0;
        int dis_hamming = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                k++;
                if (tiles[i][j] == 0) {
                } else if (k != tiles[i][j]) {
                    dis_hamming++;
                }

            }
        }
        return dis_hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dis_manhattan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tmp = tiles[i][j];
                if (tmp == 0) {
                } else {
                    int x = (tmp - 1) / N;
                    int y = (tmp - 1) % N;
                    dis_manhattan += Math.abs(x - i) + Math.abs(y - j);
                }
            }
        }
        return dis_manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != i * N + j + 1)  // blocks[i][j]位置上的元素放错
                    return false;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (!Arrays.equals(this.tiles, that.tiles)) return false;
        return true;
    }

    private int[][] copy() // 拷贝棋盘元素
    {
        int[][] newblocks = new int[N][N];
        for (int i1 = 0; i1 < N; i1++)
            for (int j1 = 0; j1 < N; j1++)
                newblocks[i1][j1] = this.tiles[i1][j1];
        return newblocks;
    }

    private Board swap(int i1, int j1, int i2, int j2) {
        int[][] newblocks = copy();
        int temp = newblocks[i1][j1];
        newblocks[i1][j1] = newblocks[i2][j2];
        newblocks[i2][j2] = temp;
        return new Board(newblocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards;
        boards = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    if (i > 0) {
                        //head
                        Board upBoard = swap(i, j, i - 1, j);
                        boards.add(upBoard);
                    }
                    if (i < N - 1) {
                        //bottom
                        Board lowBoard = swap(i, j, i + 1, j);
                        boards.add(lowBoard);
                    }
                    if (j > 0) {
                        //left
                        Board leftBoard = swap(i, j, i, j - 1);
                        boards.add(leftBoard);
                    }

                    if (j < N - 1) {
                        //right
                        Board rightBoard = swap(i, j, i, j + 1);
                        boards.add(rightBoard);
                    }
                }
            }
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i1 = 0, j1 = 0, i2 = 1, j2 = 1;
        if (tiles[i1][j1] == 0) {
            i1 = 1;
            j1 = 0;
        }
        if (tiles[i2][j2] == 0) {
            i2 = 1;
            j2 = 0;
        }

        Board newBoard = swap(i1, j1, i2, j2);

        return newBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
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

        System.out.println(board.manhattan());
        System.out.println(board.toString());
        for (Board it : board.neighbors()) {
            //System.out.println(it.toString());
        }

        System.out.println(board.twin().toString());

    }

}
