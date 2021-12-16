package aoc

import java.lang.Integer.max

class Day13 {
    companion object {
        enum class Axis {
            X, Y
        }

        private fun readInput(input: List<String>): Pair<ArrayList<Pair<Int, Int>>, ArrayList<Pair<Axis, Int>>> {
            val coords = ArrayList<Pair<Int, Int>>()
            val folds = ArrayList<Pair<Axis, Int>>()

            val inputIt = input.iterator()
            while (inputIt.hasNext()) {
                val line = inputIt.next()
                if (line.isEmpty()) break // read folds

                val (x, y) = line.split(',').map { it.toInt() }
                coords.add(Pair(x, y))
            }

            while (inputIt.hasNext()) {
                val line = inputIt.next()
                val (dir, pos) = line.split('=')

                val axis = if (dir.endsWith('x')) Axis.X else Axis.Y
                folds.add(Pair(axis, pos.toInt()))
            }

            return Pair(coords, folds)
        }

        private fun makeGrid(coords: List<Pair<Int, Int>>): ArrayList<ArrayList<Char>> {
            var (maxX, maxY) = arrayListOf(0, 0)
            coords.forEach { (x, y) ->
                maxX = max(maxX, x)
                maxY = max(maxY, y)
            }

            val grid = ArrayList<ArrayList<Char>>()
            for (i in 0..maxY) {
                val line = ArrayList<Char>()
                for (j in 0..maxX) {
                    line.add('.')
                }
                grid.add(line)
            }

            coords.forEach { (x, y) -> grid[y][x] = '#' }

            return grid
        }

        private fun foldGrid(grid: List<List<Char>>, fold: Pair<Axis, Int>): ArrayList<ArrayList<Char>> {
            val (axis, pos) = fold
            val newGrid = ArrayList<ArrayList<Char>>()
            when (axis) {
                Axis.X -> {
                    for (i in grid.indices) {
                        val line = ArrayList<Char>()
                        for (j in grid[0].indices) {
                            if (j < pos) line.add(grid[i][j])
                            else if (grid[i][j] == '#') line[2*pos-j] = '#'
                        }
                        newGrid.add(line)
                    }
                }
                Axis.Y -> {
                    for (i in grid.indices) {
                        if (i < pos) {
                            val line = ArrayList<Char>()
                            for (j in grid[0].indices) {
                                line.add(grid[i][j])
                            }
                            newGrid.add(line)
                        } else {
                            for (j in grid[0].indices) {
                                if (grid[i][j] == '#') newGrid[2*pos-i][j] = '#'
                            }
                        }
                    }
                }
            }
            return newGrid
        }

        private fun countPoints(grid: List<List<Char>>): Int {
            return grid.fold(0) { gAcc, line ->
                gAcc + line.fold(0) { lAcc, point ->
                    lAcc + if (point == '#') 1 else 0
                }
            }
        }

        fun part1(input: List<String>): Int {
            val (coords, folds) = readInput(input)
            val grid = makeGrid(coords)
            val nextGrid = foldGrid(grid, folds[0])
            return countPoints(nextGrid)
        }

        fun part2(input: List<String>): Int {
            val (coords, folds) = readInput(input)
            var nextGrid = makeGrid(coords)
            for (fold in folds) {
                nextGrid = foldGrid(nextGrid, fold)
            }

            for (line in nextGrid) { println(line) }

            return -1
        }
    }
}