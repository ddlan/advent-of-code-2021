package aoc

import kotlin.math.pow

class Day20 {
    companion object {
        private fun readInput(input: List<String>): Pair<List<Char>, MutableList<MutableList<Char>>> {
            val enhancement = input.first().map { it }
            val grid = input.drop(2).map { line -> line.map { it }.toMutableList() }.toMutableList()

            return Pair(enhancement, grid)
        }

        private fun copyGrid(grid: List<List<Char>>): MutableList<MutableList<Char>> {
            return grid.map { line -> line.map { it }.toMutableList() }.toMutableList()
        }

        private fun enhance(grid: List<List<Char>>, enhancement: List<Char>): MutableList<MutableList<Char>> {
            val gridCopy = copyGrid(grid)

            for (i in 1 until grid.size-1) {
                for (j in 1 until grid.first().size-1) {
                    var binNum = 0
                    for (k in i+1 downTo i-1) {
                        for (l in j+1 downTo j-1) {
                            if (grid[k][l] == '#') {
                                binNum += 2.0.pow(((i+1-k) * 3 + (j+1-l)).toDouble()).toInt()
                            }
                        }
                    }
//                    println("ij $i $j binNum $binNum")
                    gridCopy[i][j] = enhancement[binNum]
                }
            }

            return gridCopy
        }

        private fun widen(grid: MutableList<MutableList<Char>>, newChar: Char) {
            // top
            grid.add(0, (0..grid.first().lastIndex).map { newChar }.toMutableList())
            // left
            grid.forEach { it.add(0, newChar) }
            // right
            grid.forEach { it.add(newChar) }
            // bottom
            grid.add((0..grid.first().lastIndex).map { newChar }.toMutableList())
        }

        fun part1(input: List<String>): Int {
            val (enhancement, grid) = readInput(input)

            var currGrid = grid
            for (i in 0 until 2 ) {
                val newChar = if (enhancement[0] == '#' && i % 2 == 1) '#' else '.'
                widen(currGrid, newChar)

//                for (line in currGrid) {
//                    println(line)
//                }

                currGrid = enhance(currGrid, enhancement)

            }

            for (line in currGrid) {
                println(line)
            }

            return currGrid.fold(0) { gacc, line -> gacc + line.fold(0) { lacc, char -> lacc + if (char == '#') 1 else 0 } }
        }

        fun part2(input: List<String>): Int {
            return input.size
        }
    }
}