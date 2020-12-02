import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;
import java.util.List;


public class Percolation {
    private final int boardSize;
    private int numOpenSites;
    private final int[] openRows;
    private final int[] topRows;
    private final WeightedQuickUnionUF uf;
    private final int[] bottomRows;
    private final static int OUT_OF_BOUND = -1;
    private final static int OPEN = 1;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        boardSize = n;
        numOpenSites = 0;
        openRows = new int[n * n];
        bottomRows = new int[n];
        topRows = new int[n];
        uf = new WeightedQuickUnionUF(n * n);

        for (int i = 0; i < n; i++) {
            topRows[i] = OUT_OF_BOUND;
            bottomRows[i] = OUT_OF_BOUND;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        final int curCell = getKey(row, col);
        if (isOpen(row, col)) {
            // already open. Do nothing.
            return;
        }

        // store top row
        if (row == 0) {
            topRows[col] = curCell;
        }
        // store bottom row
        if (row == boardSize - 1) {
            bottomRows[col] = curCell;
        }
        openRows[curCell] = OPEN;
        numOpenSites++;
        final List<Integer> directions = Arrays.asList(
                isOutOfVerticalEdge(col - 1) ? OUT_OF_BOUND : getKey(row, col - 1), //left
                getKey(row - 1, col), // up
                isOutOfVerticalEdge(col + 1) ? OUT_OF_BOUND : getKey(row, col + 1), //right
                getKey(row + 1, col)); //down
        for (Integer neighborCell : directions) {
            if (!isOutOfBounds(neighborCell) && isOpen(neighborCell)) {
                uf.union(curCell, neighborCell);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return openRows[getKey(row, col)] == OPEN;
    }

    private boolean isOpen(int n) {
        return openRows[n] == OPEN;
    }

    // is the site (row, col) full? -> percolates up
    public boolean isFull(int row, int col) {
        for (int topRow : topRows) {
            if (isOutOfBounds(topRow)) {
                continue;
            }
            if (uf.find(topRow) == uf.find(getKey(row, col))) {
                return true;
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // check if any bottom rows are connected to a top row.
        for (int bottomRow : bottomRows) {
            if (isOutOfBounds(bottomRow)) {
                continue;
            }
            for (int topRow : topRows) {
                if (isOutOfBounds(topRow)) {
                    continue;
                }
                if (uf.find(topRow) == uf.find(bottomRow)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getKey(int row, int col) {
        return (boardSize * row) + col;
    }

    private boolean isOutOfBounds(final int n) {
        return n < 0 || n >= boardSize * boardSize;
    }

    private boolean isOutOfVerticalEdge(final int col) {
        return col < 0 || col > boardSize - 1;
    }
}
