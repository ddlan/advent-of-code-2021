package aoc

class Day09 {
    companion object {
        private fun adjacentSpaces(y: Int, x: Int, board: List<List<Int>>): List<Pair<Int, Int>> {
            val height = board.size
            val width = board[0].size

            return arrayListOf(Pair(y-1, x), Pair(y, x-1), Pair(y+1, x), Pair(y, x+1)).filter(fun(pair): Boolean {
                val (ay, ax) = pair
                return ay in 0 until height && ax in 0 until width
            })
        }

        fun part1(input: List<String>): Int {
            val board = input.map { line -> line.map { char -> Character.getNumericValue(char) } }
            val height = board.size
            val width = board[0].size

            fun riskAt(y: Int, x: Int): Int {
                val adjacent = adjacentSpaces(y, x, board)
                for ((ay, ax) in adjacent) {
                    if (board[ay][ax] <= board[y][x]) return 0
                }

                return board[y][x] + 1
            }


            var totalRisk = 0
            for (y in 0 until height) {
                for (x in 0 until width) {
                    totalRisk += riskAt(y, x)
                }
            }

            return totalRisk
        }

        fun part2(input: List<String>): Int {
            val board = input.map { line -> line.map { char -> Character.getNumericValue(char) } } as ArrayList<ArrayList<Int>>
            val height = board.size
            val width = board[0].size

            fun countBasin(y: Int, x: Int): Int {
                if (board[y][x] == 9) return 0

                val adjacent = adjacentSpaces(y, x, board)
                board[y][x] = 9
                var basinSize = 1
                adjacent.forEach(fun(pair) {
                    val (ay, ax) = pair
                    basinSize += countBasin(ay, ax)
                })
                return basinSize
            }

            fun basinAt(y: Int, x: Int): Int {
                val adjacent = adjacentSpaces(y, x, board)
                for ((ay, ax) in adjacent) {
                    if (board[ay][ax] <= board[y][x]) return 0
                }

                // We have a basin
                return countBasin(y, x)
            }


            val threeLargestBasins = arrayListOf(0, 0, 0)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (board[y][x] == 9) continue
                    threeLargestBasins.add(basinAt(y, x))
                    threeLargestBasins.remove(threeLargestBasins.minOrNull())
                }
            }

            return threeLargestBasins.reduce { acc, basin -> acc * basin }
        }
    }
}
