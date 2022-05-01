package com.algorithms.percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE = 1.96;
    private final double mean;
    private final double standardDeviation;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        double[] experiments = new double[trials];
        for(int i = 0; i < trials; i++)
            experiments[i] = runSimulation(n) / (n * n);

        mean = StdStats.mean(experiments);
        standardDeviation = StdStats.stddev(experiments);
        this.trials = trials;
    }

    private double runSimulation(int n) {
        Percolation percolation = new Percolation(n);

        do {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);

            percolation.open(row, col);
        } while (!percolation.percolates());

        return percolation.numberOfOpenSites();
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return standardDeviation;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONFIDENCE * standardDeviation / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONFIDENCE * standardDeviation / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.printf("Mean: %f%n", percolationStats.mean());
        StdOut.printf("Standard Deviation: %f%n", percolationStats.stddev());
        StdOut.printf("95%% Confidence Interval: [%f,%f] %n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
