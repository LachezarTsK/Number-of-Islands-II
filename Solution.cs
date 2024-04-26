
using System;
using System.Collections.Generic;

public class Solution
{
    private static readonly int[][] moves =
        { new int[]{ -1, 0 }, new int[]{ 1, 0 }, new int[] { 0, -1 }, new int[]{ 0, 1 } };

    private static readonly int WATER = 0;
    private static readonly int LAND = 1;

    private int[,] matrix;
    private int rows;
    private int columns;
    int countIslands = 0;

    public IList<int> NumIslands2(int rows, int columns, int[][] positions)
    {
        matrix = new int[rows, columns];
        this.rows = rows;
        this.columns = columns;

        UnionFind unionFind = new UnionFind(rows * columns);
        IList<int> numberOfIslandsAtTimeSnapshot = new List<int>();

        foreach (int[] position in positions)
        {
            int row = position[0];
            int column = position[1];

            if (matrix[row, column] == LAND)
            {
                numberOfIslandsAtTimeSnapshot.Add(countIslands);
                continue;
            }

            ++countIslands;
            matrix[row, column] = LAND;

            foreach (int[] move in moves)
            {
                int nextRow = row + move[0];
                int nextColumn = column + move[1];

                if (IsInMatrix(nextRow, nextColumn) && matrix[nextRow, nextColumn] == LAND)
                {
                    UpdateIslands(GetIndex(row, column), GetIndex(nextRow, nextColumn), unionFind);
                }
            }
            numberOfIslandsAtTimeSnapshot.Add(countIslands);
        }
        return numberOfIslandsAtTimeSnapshot;
    }

    private void UpdateIslands(int currentIndex, int adjacentIndex, UnionFind unionFind)
    {
        int currentParent = unionFind.FindParent(currentIndex);
        int adjacentParent = unionFind.FindParent(adjacentIndex);

        if (adjacentParent != currentParent)
        {
            unionFind.JoinByRank(currentParent, adjacentParent);
            --countIslands;
        }
    }

    private int GetIndex(int row, int column)
    {
        return row * columns + column;
    }

    private bool IsInMatrix(int row, int column)
    {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
}

class UnionFind
{
    public int[] parent;
    public int[] rank;

    public UnionFind(int numberOfPositions)
    {
        parent = new int[numberOfPositions + 1];
        rank = new int[numberOfPositions + 1];

        for (int i = 0; i < numberOfPositions + 1; ++i)
        {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int FindParent(int index)
    {
        if (parent[index] != index)
        {
            parent[index] = FindParent(parent[index]);
        }
        return parent[index];
    }

    public void JoinByRank(int first, int second)
    {
        if (rank[first] >= rank[second])
        {
            parent[second] = parent[first];
            rank[first] += rank[second];
        }
        else
        {
            parent[first] = parent[second];
            rank[second] += rank[first];
        }
    }
}
