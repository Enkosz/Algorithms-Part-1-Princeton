package com.algorithms.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF quickUnionUF;
    private final boolean[][] data;
    private final int n;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if(n <= 0) throw new IllegalArgumentException("Invalid n with value: " + n + ", it must be >= 0");

        this.n = n;
        this.quickUnionUF = new WeightedQuickUnionUF(getMatrixSize() + 2); // +2 because we have virtual top and bottom
        this.data = new boolean[n][n];
        this.openSites = 0;

        this.connectToVirtual();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateCoordinates(row, col);

        // skip when is already open and coordinates are valid
        if(areValidCoordinates(row, col) && isOpen(row, col))
            return;

        int point = get1DCoordinates(row, col);

        // top
        if(areValidCoordinates(row - 1, col) && isOpen(row - 1, col))
            quickUnionUF.union(point, get1DCoordinates(row - 1, col));
        // bottom
        if(areValidCoordinates(row + 1, col) && isOpen(row + 1, col))
            quickUnionUF.union(point, get1DCoordinates(row + 1, col));
        // left
        if(areValidCoordinates(row, col - 1) && isOpen(row, col - 1))
            quickUnionUF.union(point, get1DCoordinates(row, col - 1));
        // right
        if(areValidCoordinates(row, col + 1) && isOpen(row, col + 1))
            quickUnionUF.union(point, get1DCoordinates(row, col + 1));

        // 0-index based conversion
        data[row - 1][col - 1] = true;
        this.openSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateCoordinates(row, col);

        return this.data[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateCoordinates(row, col);

        return quickUnionUF.connected(0, get1DCoordinates(row, col)) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // TODO Can optimize by saving when it percolates in a variable and updating it when opening a node
        return quickUnionUF.connected(0, getMatrixSize() + 1);
    }

    // connects the top and bottom rows with a virtual node
    private void connectToVirtual() {
        int size = getMatrixSize();

        // We start from 1 since we also have virtual nodes
        for(int i = 1; i <= this.n; i++) {
            quickUnionUF.union(0, i);
            quickUnionUF.union(size + 1, size + 1 - i);
        }
    }

    private int getMatrixSize() {
        return this.n * this.n;
    }

    private boolean areValidCoordinates(int row, int col) {
        return row > 0 && row <= this.n && col > 0 && col <= this.n;
    }

    private void validateCoordinates(int row, int col) {
        if(!areValidCoordinates(row, col))
            throw new IllegalArgumentException("Invalid coordinates");
    }

    // Covert 1-index 2D based coordinates into 1D 0-index based coordinates
    private int get1DCoordinates(int row, int col) {
        return (row - 1) * this.n + col;
    }

    // test client (optional)
    public static void main(String[] args) {}
}
