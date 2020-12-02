public interface IPercolation {

    // opens the site (row, col) if it is not open already
    void open(int row, int col);

    // is the site (row, col) open?
    boolean isOpen(int row, int col);

    // is the site (row, col) full?
    boolean isFull(int row, int col);

    // returns the number of open sites
    int numberOfOpenSites();

    // does the system percolate?
    boolean percolates();
}
