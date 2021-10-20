import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trails;
    private final double[] mean;

    // perform independent trals on an n-by-n grid
    public PercolationStats(int n, int trails) {
        this.trails = trails;
        validate(n, trails);

        mean = new double[trails];
        Percolation per;
        for (int i = 0; i < trails; i++) {
            //initialize the array each time
            per = new Percolation(n);
            double count = 0;
            //repeating this computation experiment T times
            while (!per.percolates()) {
                per.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
                count = per.numberOfOpenSites();
                if (per.percolates()) {
                    break;
                }
            }
            //averaging the results
            mean[i] = count / (n * n);
        }

    }

    private void validate(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("The number has been a positive number.");
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(mean);
    }
    //
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(mean);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96*stddev()) / Math.sqrt(trails);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96*stddev()) / Math.sqrt(trails);
    }

    public static void main(String[] args) {
        PercolationStats perstts = new PercolationStats(2, 10000);
        System.out.println("mean=" + perstts.mean());
        System.out.println("stddev=" + perstts.stddev());
        System.out.println("95% confidence interval=" + "["+perstts.confidenceLo() +
                "," + perstts.confidenceHi() + "]");
    }
}
