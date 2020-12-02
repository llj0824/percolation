import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PercolationWeightedUnion implements Percolation {
    private final int boardSize;
    private final Set<Integer> openRows;
    private final Set<Integer> topRows;
    private final WeightedQuickUnionUF uf;
    private final Set<Integer> bottomRows;
    private final static int OUT_OF_BOUND = -1;

    // creates n-by-n grid, with all sites initially blocked
    public PercolationWeightedUnion(int n) {
        boardSize = n;
        openRows = new HashSet<>();
        bottomRows = new HashSet<>();
        topRows = new HashSet<>();
        uf = new WeightedQuickUnionUF(n * n);
    }

    // opens the site (row, col) if it is not open already
    @Override
    public void open(int row, int col) {
        final int curCell = getKey(row, col);
        if (isOpen(row, col)) {
            // already open. Do nothing.
            return;
        }

        // store top row
        if (row == 0) {
            topRows.add(curCell);
        }
        // store bottom row
        if (row == boardSize - 1) {
            bottomRows.add(curCell);
        }
        openRows.add(curCell);
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
    @Override
    public boolean isOpen(int row, int col) {
        return openRows.contains(getKey(row, col));
    }

    boolean isOpen(int n) {
        return openRows.contains(n);
    }

    // is the site (row, col) full? -> percolates up
    @Override
    public boolean isFull(int row, int col) {
        return topRows.stream()
                .anyMatch(topRow -> uf.find(topRow) == uf.find(getKey(row, col)));
    }

    // returns the number of open sites
    @Override
    public int numberOfOpenSites() {
        return uf.count();
    }

    // does the system percolate?
    @Override
    public boolean percolates() {
        // check if any bottom rows are connected to a top row.
        return bottomRows.stream()
                .anyMatch(bottomRow -> {
                    for (Integer topRow : topRows) {
                        if (uf.find(topRow) == uf.find(bottomRow)) {
                            return true;
                        }
                    }
                    return false;
                });
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
