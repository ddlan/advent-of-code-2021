package aoc

class Day25 {
    companion object {
        private fun readInput(input: List<String>): List<List<Char>> {
            return input.map { line -> line.map { it } }
        }

        private fun step(grid: List<List<Char>>): List<List<Char>> {
            val (height, width) = listOf(grid.size, grid.first().size)

            val partial = grid.map { line -> line.map { it }.toMutableList() }

            // east step
            for (i in grid.indices) {
                for (j in grid[0].indices) {
                    val nextJ = (j+1) % width
                    if (grid[i][j] == '>' && grid[i][nextJ] == '.') {
                        partial[i][j] = '.'
                        partial[i][nextJ] = '>'
                    }
                }
            }

            val result = partial.map { line -> line.map { it }.toMutableList() }

            // south step
            for (i in partial.indices) {
                for (j in partial[0].indices) {
                    val nextI = (i+1) % height
                    if (partial[i][j] == 'v' && partial[nextI][j] == '.') {
                        result[i][j] = '.'
                        result[nextI][j] = 'v'
                    }
                }
            }

            return result
        }

        private fun sameGrids(grid: List<List<Char>>, other: List<List<Char>>): Boolean {
            for (i in grid.indices) {
                for (j in grid.first().indices) {
                    if (grid[i][j] != other[i][j]) return false
                }
            }
            return true
        }

        fun part1(input: List<String>): Int {
            var iteration = 0
            var curr = readInput(input)

            while (true) {
                val next = step(curr)
                iteration++
                if (sameGrids(next, curr)) break
                curr = next
            }

            return iteration
        }

        fun part2(input: List<String>): Int {
            return input.size
        }
    }
}