
/**
 * @param {number} rows
 * @param {number} columns
 * @param {number[][]} positions
 * @return {number[]}
 */
var numIslands2 = function (rows, columns, positions) {
    this.moves = [[-1, 0], [1, 0], [0, -1], [0, 1]];
    this.WATER = 0;
    this.LAND = 1;

    this.matrix = Array.from(new Array(rows), () => new Array(columns).fill(this.WATER));
    this.rows = rows;
    this.columns = columns;
    this.countIslands = 0;

    const unionFind = new UnionFind(rows * columns);
    const numberOfIslandsAtTimeSnapshot = new Array();

    for (let [row, column] of positions) {
        if (this.matrix[row][column] === this.LAND) {
            numberOfIslandsAtTimeSnapshot.push(this.countIslands);
            continue;
        }

        ++this.countIslands;
        this.matrix[row][column] = this.LAND;

        for (let move of this.moves) {
            let nextRow = row + move[0];
            let nextColumn = column + move[1];

            if (isInMatrix(nextRow, nextColumn) && this.matrix[nextRow][nextColumn] === this.LAND) {
                updateIslands(getIndex(row, column), getIndex(nextRow, nextColumn), unionFind);
            }
        }
        numberOfIslandsAtTimeSnapshot.push(this.countIslands);
    }
    return numberOfIslandsAtTimeSnapshot;
};

/**
 * @param {number} currentIndex
 * @param {number} adjacentIndex
 * @param {UnionFind} unionFind 
 * @return {void}
 */
function updateIslands(currentIndex, adjacentIndex, unionFind) {
    let currentParent = unionFind.findParent(currentIndex);
    let adjacentParent = unionFind.findParent(adjacentIndex);

    if (adjacentParent !== currentParent) {
        unionFind.joinByRank(currentParent, adjacentParent);
        --this.countIslands;
    }
}

/**
 * @param {number} row
 * @param {number} column
 * @return {number}
 */
function getIndex(row, column) {
    return row * this.columns + column;
}

/**
 * @param {number} row
 * @param {number} column
 * @return {boolean}
 */
function isInMatrix(row, column) {
    return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
}

class UnionFind {

    /**
     * @param {number} numberOfPositions
     */
    constructor(numberOfPositions) {
        this.parent = new Array(numberOfPositions + 1);
        this.rank = new Array(numberOfPositions + 1);
        for (let i = 0; i < numberOfPositions + 1; ++i) {
            this.parent[i] = i;
            this.rank[i] = 1;
        }
    }

    /**
     * @param {number} index
     * @return {number}
     */
    findParent(index) {
        if (this.parent[index] !== index) {
            this.parent[index] = this.findParent(this.parent[index]);
        }
        return this.parent[index];
    }

    /**
     * @param {number} first
     * @param {number} second
     * @return {void}
     */
    joinByRank(first, second) {
        if (this.rank[first] >= this.rank[second]) {
            this.parent[second] = this.parent[first];
            this.rank[first] += this.rank[second];
        } else {
            this.parent[first] = this.parent[second];
            this.rank[second] += this.rank[first];
        }
    }
}
