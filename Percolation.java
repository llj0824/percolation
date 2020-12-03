import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int boardSize;
    private int numOpenSites;
    private final boolean[] openRows;
    private final int virtualLinkTopRows = 0; // is connected to all the top rows. Set to unused index [0-boardSize]. Board starts at [1,1], or [boardSize, boardSize^2-1]
    private final int virtualLinkBottomRows = 1; // is connected to all the bottom rows
    private final WeightedQuickUnionUF uf;
    private final static int OUT_OF_BOUND = -1;
    private final static int UNEXPLORED = -1;
    private final static int FIRST_INDEX = 1;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Percolation initialization value must be greater than 0.");
        }
        n += 1; // account for index starting at one.
        boardSize = n;
        numOpenSites = 0;
        openRows = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateInput(row, col);
        final int curCell = getKey(row, col);
        if (isOutOfBounds(curCell) || isOpen(row, col)) {
            // already open. Do nothing.
            return;
        }

        // store top row
        if (row == FIRST_INDEX) {
            uf.union(virtualLinkTopRows, curCell);
        }
        // store bottom row
        if (row == boardSize - 1) {
            uf.union(virtualLinkBottomRows, curCell);
        }
        openRows[curCell] = true;
        numOpenSites++;
        final int[] directions = {
                isOutOfVerticalEdge(col - 1) ? OUT_OF_BOUND : getKey(row, col - 1), //left
                getKey(row - 1, col), // up
                isOutOfVerticalEdge(col + 1) ? OUT_OF_BOUND : getKey(row, col + 1), //right
                getKey(row + 1, col) //down
        }; //might be performance bug
        for (Integer neighborCell : directions) {
            if (!isOutOfBounds(neighborCell) && isOpen(neighborCell)) {
                uf.union(curCell, neighborCell);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateInput(row, col);
        return openRows[getKey(row, col)] == true;
    }

    private boolean isOpen(int n) {
        return openRows[n] == true;
    }

    // is the site (row, col) full? -> percolates up
    public boolean isFull(int row, int col) {
        validateInput(row, col);
        if (isOutOfBounds(getKey(row,col))) {
            return false;
        }
        return isOutOfBounds(getKey(row,col)) && (uf.find(virtualLinkTopRows) == uf.find(getKey(row, col)));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualLinkTopRows) == uf.find(virtualLinkBottomRows);
    }

    private int getKey(int row, int col) {
        return (boardSize * row) + col;
    }

    private boolean isOutOfBounds(final int n) {
        return n < FIRST_INDEX || n >= boardSize * boardSize;
    }

    private boolean isOutOfVerticalEdge(final int col) {
        return col < FIRST_INDEX || col > boardSize - 1;
    }

    private void validateInput(final int row, final int col) {
        if (row < FIRST_INDEX || col < FIRST_INDEX || row >= boardSize || col >= boardSize) {
            throw new IllegalArgumentException();
        }
    }
}
