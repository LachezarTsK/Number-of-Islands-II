
#include <array>
#include <vector>
using namespace std;

class UnionFind {

public:
    vector<size_t> parent;
    vector<size_t> rank;

    explicit UnionFind(size_t numberOfPositions) {
        parent.resize(numberOfPositions + 1);
        rank.resize(numberOfPositions + 1);

        for (size_t i = 0; i < numberOfPositions + 1; ++i) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    size_t findParent(size_t index) {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index]);
        }
        return parent[index];
    }

    void joinByRank(size_t first, size_t second) {
        if (rank[first] >= rank[second]) {
            parent[second] = parent[first];
            rank[first] += rank[second];
        }
        else {
            parent[first] = parent[second];
            rank[second] += rank[first];
        }
    }
};

class Solution {

    inline static const array<array<int, 2>, 4> moves{ { {-1, 0}, {1, 0}, {0, -1}, {0, 1} } };
    static const int WATER = 0;
    static const int LAND = 1;

    vector<vector<int>> matrix;
    size_t rows = 0;
    size_t columns = 0;
    int countIslands = 0;

public:
    vector<int> numIslands2(int rows, int columns, const vector<vector<int>>& positions) {
        matrix.resize(rows, vector<int>(columns));
        this->rows = rows;
        this->columns = columns;

        UnionFind unionFind(rows * columns);
        vector<int> numberOfIslandsAtTimeSnapshot;

        for (const auto& position : positions) {
            size_t row = position[0];
            size_t column = position[1];

            if (matrix[row][column] == LAND) {
                numberOfIslandsAtTimeSnapshot.push_back(countIslands);
                continue;
            }

            ++countIslands;
            matrix[row][column] = LAND;

            for (const auto& move : moves) {
                size_t nextRow = row + move[0];
                size_t nextColumn = column + move[1];

                if (isInMatrix(nextRow, nextColumn) && matrix[nextRow][nextColumn] == LAND) {
                    updateIslands(getIndex(row, column), getIndex(nextRow, nextColumn), unionFind);
                }
            }
            numberOfIslandsAtTimeSnapshot.push_back(countIslands);
        }
        return numberOfIslandsAtTimeSnapshot;
    }

    void updateIslands(size_t currentIndex, size_t adjacentIndex, UnionFind& unionFind) {
        size_t currentParent = unionFind.findParent(currentIndex);
        size_t adjacentParent = unionFind.findParent(adjacentIndex);

        if (adjacentParent != currentParent) {
            unionFind.joinByRank(currentParent, adjacentParent);
            --countIslands;
        }
    }

    size_t getIndex(size_t row, size_t column) const {
        return row * columns + column;
    }

    bool isInMatrix(size_t row, size_t column) const {
        return row < rows && column < columns;
    }
};
