package aoc

class Day11 {
    companion object {
        private fun readGrid(input: List<String>): List<ArrayList<Int>> {
            return input.map { line ->
                ArrayList(line.map { char -> Character.getNumericValue(char) })
            }
        }

        private fun step(grid: List<ArrayList<Int>>): Int {
            val height = grid.size
            val width = grid[0].size

            fun adjacentSpaces(y: Int, x: Int): List<Pair<Int, Int>> {
                return arrayListOf(
                    Pair(y-1, x-1), Pair(y-1, x), Pair(y-1, x+1),
                    Pair(y  , x-1),               Pair(y  , x+1),
                    Pair(y+1, x-1), Pair(y+1, x), Pair(y+1, x+1))
                    .filter(fun(pair): Boolean {
                        val (ay, ax) = pair
                        return ay in 0 until height && ax in 0 until width
                    })
            }

            // 1. Energy
            grid.forEach { line -> line.forEachIndexed { i, _ -> line[i]++ } }

            // 2. Flash & Reset
            var flashes = 0

            fun flash(y: Int, x: Int) {
                flashes++
                grid[y][x] = 0

                for ((ay, ax) in adjacentSpaces(y, x)) {
                    if (grid[ay][ax] == 0) continue
                    grid[ay][ax]++
                    if (grid[ay][ax] > 9) { flash(ay, ax) }
                }
            }

            grid.forEachIndexed { y, line ->
                line.forEachIndexed { x, energy ->
                    if (energy > 9) { flash(y, x) }
                }
            }

            return flashes
        }

        fun part1(input: List<String>): Int {
            val grid = readGrid(input)
            var flashes = 0

            for (i in 0 until 100) {
                flashes += step(grid)
            }

            return flashes
        }

        fun part2(input: List<String>): Int {
            val grid = readGrid(input)
            val totalOctopi = grid.size * grid[0].size

            for (step in 1 until 100000) {
                if (step(grid) == totalOctopi) {
                    return step
                }
            }

            return -1
        }
    }
}