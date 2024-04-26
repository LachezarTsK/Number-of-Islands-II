
class Solution {

    companion object {
        val moves = listOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
        const val WATER = 0;
        const val LAND = 1;
    }

    private var matrix = ArrayList<IntArray>()
    private var rows = 0;
    private var columns = 0
    private var countIslands = 0;

    fun numIslands2(rows: Int, columns: Int, positions: Array<IntArray>): List<Int> {
        matrix = ArrayList<IntArray>(rows)
        for (i in 0..rows) {
            matrix.add(IntArray(columns))
        }
        this.rows = rows;
        this.columns = columns;

        val unionFind = UnionFind(rows * columns);
        val numberOfIslandsAtTimeSnapshot = ArrayList<Int>();

        for ((row, column) in positions) {
            if (matrix[row][column] == LAND) {
                numberOfIslandsAtTimeSnapshot.add(countIslands);
                continue;
            }

            ++countIslands;
            matrix[row][column] = LAND;

            for (move in moves) {
                val nextRow = row + move[0];
                val nextColumn = column + move[1];

                if (isInMatrix(nextRow, nextColumn) && matrix[nextRow][nextColumn] == LAND) {
                    updateIslands(getIndex(row, column), getIndex(nextRow, nextColumn), unionFind);
                }
            }
            numberOfIslandsAtTimeSnapshot.add(countIslands);
        }
        return numberOfIslandsAtTimeSnapshot;
    }

    private fun updateIslands(currentIndex: Int, adjacentIndex: Int, unionFind: UnionFind) {
        val currentParent = unionFind.findParent(currentIndex);
        val adjacentParent = unionFind.findParent(adjacentIndex);

        if (adjacentParent != currentParent) {
            unionFind.joinByRank(currentParent, adjacentParent);
            --countIslands;
        }
    }

    private fun getIndex(row: Int, column: Int): Int {
        return row * columns + column;
    }

    private fun isInMatrix(row: Int, column: Int): Boolean {
        return row in 0..<rows && column in 0..<columns
    }
}

class UnionFind(private val numberOfPositions: Int) {

    private val parent = IntArray(numberOfPositions + 1)
    private val rank = IntArray(numberOfPositions + 1)

    init {
        for (i in 0..<numberOfPositions + 1) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    fun findParent(index: Int): Int {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index]);
        }
        return parent[index];
    }

    fun joinByRank(first: Int, second: Int) {
        if (rank[first] >= rank[second]) {
            parent[second] = parent[first];
            rank[first] += rank[second];
        } else {
            parent[first] = parent[second];
            rank[second] += rank[first];
        }
    }
}
