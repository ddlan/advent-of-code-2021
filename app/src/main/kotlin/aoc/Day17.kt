package aoc

class Day17 {
    companion object {
        fun part1(input: List<String>): Int {
            val string = input.first().drop(15)
            val (_, _, yMin, _) = string.split("..", ", y=").map { it.toInt() }

            return (yMin * (yMin+1)) / 2
        }

        fun part2(input: List<String>): Int {
            val string = input.first().drop(15)
            val (xMin, xMax, yMin, yMax) = string.split("..", ", y=").map { it.toInt() }

            fun validTrajectory(vx: Int, vy: Int): Boolean {
                var (cvx, cvy) = listOf(vx, vy)
                var (x, y) = listOf(0, 0)

                while (true) {
                    if (x in xMin..xMax && y in yMin..yMax)  return true
                    if (x > xMax || y < yMin) return false

                    x += cvx
                    y += cvy

                    if (cvx > 0) cvx--
                    cvy--
                }
            }

            var validCount = 0
            for (i in 0..xMax) {
                for (j in yMin..-yMin) {
                    if (validTrajectory(i, j)) validCount += 1
                }
            }

            return validCount
        }
    }
}