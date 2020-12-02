import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class PercolationStats {
    final double elapsedTime;
    private final int[] percolationThresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        final Stopwatch stopwatch = new Stopwatch();
        if (n <= 0 || trials < 0) {
            throw new IllegalArgumentException("PercolationStats initialization value must be greater than 0.");
        }

        percolationThresholds = new int[trials];
        for (int t = 0; t < trials; t++) {
            System.out.println("trial: " + t);
            final Percolation trial = new Percolation(n);
            do {
                // randomly open cell
                final int row = StdRandom.uniform(n);
                final int col = StdRandom.uniform(n);
                trial.open(row, col);
            } while (trial.percolates() == false);
            System.out.println("elapsedtime: " + stopwatch.elapsedTime());
            percolationThresholds[t] = trial.numberOfOpenSites();
        }

        elapsedTime = stopwatch.elapsedTime();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double x = mean();
        double s = Math.sqrt(stddev());
        double T = Math.sqrt(percolationThresholds.length);
        double result = x - (1.96d * (s / T));
        return result;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double x = mean();
        double s = Math.sqrt(stddev());
        double T = Math.sqrt(percolationThresholds.length);
        double result =  x + 1.96d * (s/T);
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {
        final Integer n = Integer.valueOf(args[0]); // board size
        final Integer T = Integer.valueOf(args[1]); // number of simulations
        PercolationStats stats = new PercolationStats(n, T);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println(String.format("95%% confidence interval = [%f, %f]", stats.confidenceHi(), stats.confidenceLo()));
        // main() method that takes two command-line arguments n and T, performs T independent computational experiments (discussed above) on an n-by-n grid,
        // and prints the sample mean, sample standard deviation, and the 95% confidence interval for the percolation threshold.
        // Use StdRandom to generate random numbers; use StdStats to compute the sample mean and sample standard deviation.
    }
}
