import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Percolation {
    final int boardSize;
    final Set<String> topRows;
    final Map<String, Set<String>> nodeToConnectedNodesMap;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid Percolation initialization value.");
        }
        nodeToConnectedNodesMap = new HashMap<>();
        boardSize = n;
        topRows = IntStream.range(0, boardSize).boxed()
                .map(col -> getKey(0, col))
                .collect(Collectors.toSet());
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        final String curCell = getKey(row, col);
        if (isOpen(row, col) || isOutOfBounds(row, col)) {
            // already open. Do nothing.
             return;
        }

        nodeToConnectedNodesMap.put(curCell, new HashSet<>(Arrays.asList(curCell)));
        final List<String> directions = Arrays.asList(
                getKey(row, col-1), //left
                getKey(row-1, col), // up
                getKey(row, col+1), //right
                getKey(row+1, col)); //down
        for (String neighborCell : directions) {
            if (nodeToConnectedNodesMap.containsKey(neighborCell)) {
                Set<String> neighboringSet = nodeToConnectedNodesMap.get(neighborCell);
                Set<String> curSet = nodeToConnectedNodesMap.get(curCell);
                Set<String> biggerSet = neighboringSet.size() > curSet.size() ? neighboringSet :
                        curSet;
                Set<String> smallerSet = neighboringSet.size() <= curSet.size() ? neighboringSet :
                        curSet;
                biggerSet.addAll(smallerSet); // superset

                // update reference of all smaller set's elements.
                smallerSet.stream().forEach(key -> nodeToConnectedNodesMap.put(key, biggerSet));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return nodeToConnectedNodesMap.containsKey(getKey(row, col));
    }

    // is the site (row, col) full? -> percolates up
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }

        final Set<String> connectedCells = nodeToConnectedNodesMap.get(getKey(row, col));
        return topRows.stream().anyMatch(topRowCell -> connectedCells.contains(topRowCell));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nodeToConnectedNodesMap.keySet().size();
    }

    // does the system percolate?
    public boolean percolates() {
        Set<String> bottomRows = IntStream.range(0, boardSize).boxed()
                .map(col -> getKey(boardSize-1, col)).collect(Collectors.toSet());

        // check if any bottom rows are connected to a top row.
        return bottomRows.stream()
                .filter(bottomRow -> nodeToConnectedNodesMap.containsKey(bottomRow))
                .anyMatch(bottomRow -> {
                    Set<String> connectCells = nodeToConnectedNodesMap.get(bottomRow);
                    for (String topRow : topRows) {
                        if (connectCells.contains(topRow)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    private String getKey(final int row, final int col) {
        return String.format("(%d,%d)", col, row);
    }

    private boolean isOutOfBounds(final int row, final int col) {
        return row < 0 || row > boardSize || col < 0 || col > boardSize;
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}