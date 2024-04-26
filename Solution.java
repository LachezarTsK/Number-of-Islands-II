
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static final int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int WATER = 0;
    private static final int LAND = 1;

    private int[][] matrix;
    private int rows;
    private int columns;
    private int countIslands = 0;

    public List<Integer> numIslands2(int rows, int columns, int[][] positions) {
        matrix = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;

        UnionFind unionFind = new UnionFind(rows * columns);
        List<Integer> numberOfIslandsAtTimeSnapshot = new ArrayList<>();

        for (int[] position : positions) {
            int row = position[0];
            int column = position[1];

            if (matrix[row][column] == LAND) {
                numberOfIslandsAtTimeSnapshot.add(countIslands);
                continue;
            }

            ++countIslands;
            matrix[row][column] = LAND;

            for (int[] move : moves) {
                int nextRow = row + move[0];
                int nextColumn = column + move[1];

                if (isInMatrix(nextRow, nextColumn) && matrix[nextRow][nextColumn] == LAND) {
                    updateIslands(getIndex(row, column), getIndex(nextRow, nextColumn), unionFind);
                }
            }
            numberOfIslandsAtTimeSnapshot.add(countIslands);
        }
        return numberOfIslandsAtTimeSnapshot;
    }

    private void updateIslands(int currentIndex, int adjacentIndex, UnionFind unionFind) {
        int currentParent = unionFind.findParent(currentIndex);
        int adjacentParent = unionFind.findParent(adjacentIndex);

        if (adjacentParent != currentParent) {
            unionFind.joinByRank(currentParent, adjacentParent);
            --countIslands;
        }
    }

    private int getIndex(int row, int column) {
        return row * columns + column;
    }

    private boolean isInMatrix(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
}

class UnionFind {

    int[] parent;
    int[] rank;

    UnionFind(int numberOfPositions) {
        parent = new int[numberOfPositions + 1];
        rank = new int[numberOfPositions + 1];

        for (int i = 0; i < numberOfPositions + 1; ++i) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    int findParent(int index) {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index]);
        }
        return parent[index];
    }

    void joinByRank(int first, int second) {
        if (rank[first] >= rank[second]) {
            parent[second] = parent[first];
            rank[first] += rank[second];
        } else {
            parent[first] = parent[second];
            rank[second] += rank[first];
        }
    }
}
