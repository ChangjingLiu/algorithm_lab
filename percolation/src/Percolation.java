import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private final boolean[][] grid;
    private int count;
    private final int virtualTop;
    private final int virtualBot;
    // grid with top and bottom [0,n*n-1],[n*n]Top,[n*n+1]
    private final WeightedQuickUnionUF uf;
    private final int n;
    // grid with only top
    private final WeightedQuickUnionUF uf1;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n<=0){
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                grid[i][j]=false;
            }
        }
        this.n = n;
        virtualTop = 0;
        virtualBot = n*n +1;
        uf = new WeightedQuickUnionUF(n*n+2);
        uf1 = new WeightedQuickUnionUF(n * n + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        validate(row, col);

        if (!grid[row-1][col-1]) {
            grid[row-1][col-1] = true;
            //connect point at row1 to the Virtual top
            if (row == 1) {
                uf.union(trans(1, col), virtualTop);
                uf1.union(trans(1, col), virtualTop);
            }
            //connect point at rowN  to the Virtual bottom
            if (row == n) {
                uf.union(trans(n, col), virtualBot);
            }

            count++;
            // top
            if (row > 1 && isOpen(row-1, col)) {
                uf.union(trans(row, col), trans(row-1, col));
                uf1.union(trans(row, col), trans(row-1, col));
            }
            // bottom
            if (row < n && isOpen(row+1, col)) {
                uf.union(trans(row, col), trans(row+1, col));
                uf1.union(trans(row, col), trans(row+1, col));
            }
            // left
            if (col > 1 && isOpen(row, col-1)) {
                uf.union(trans(row, col), trans(row, col - 1));
                uf1.union(trans(row, col), trans(row, col - 1));
            }
            // right
            if (col < n && isOpen(row, col+1)) {
                uf.union(trans(row, col), trans(row, col + 1));
                uf1.union(trans(row, col), trans(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        validate(row, col);
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        validate(row, col);
        return uf1.find(virtualTop) == uf1.find(trans(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return count;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(virtualTop) == uf.find(virtualBot);
    }


    private void validate(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("The number is out of its border.");
        }
    }
    private int trans(int row, int col) {
        return (row-1) * n + col;
    }

    // test client (optional)
    public static void main(String[] args){
        Percolation per = new Percolation(5);
        per.open(1, 1);
        per.open(2, 1);
        per.open(3, 1);
        per.open(4, 1);
        per.open(5, 1);
        per.open(5, 3);
        per.open(4, 3);
        per.open(2, 4);
        System.out.println(per.grid[1][0]);
        System.out.println(per.numberOfOpenSites());
        System.out.println("Is this point full?" + per.isFull(4, 3));
        System.out.println("Is this point open?" + per.isOpen(4, 3));
        System.out.println("Is this model percolate?"+per.percolates());
    }
}
