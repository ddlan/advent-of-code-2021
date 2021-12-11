package aoc

class Day02 {
    companion object {
        enum class Direction {
            Forward, Up, Down
        }

        fun part1(input: List<String>): Int {
            var forwardDist = 0
            var downwardDist = 0

            for (line in input) {
                val (dir, dist) = line.split(" ")

                when (Direction.valueOf(dir)) {
                    Direction.Forward -> forwardDist += dist.toInt()
                    Direction.Up -> downwardDist -= dist.toInt()
                    Direction.Down -> downwardDist += dist.toInt()
                }
            }
            return forwardDist * downwardDist
        }

        fun part2(input: List<String>): Int {
            var aim = 0
            var forwardDist = 0
            var downwardDist = 0

            for (line in input) {
                val (dir, dist) = line.split(" ")

                when (Direction.valueOf(dir)) {
                    Direction.Forward -> {
                        forwardDist += dist.toInt()
                        downwardDist += dist.toInt() * aim
                    }
                    Direction.Up -> aim -= dist.toInt()
                    Direction.Down -> aim += dist.toInt()
                }
            }
            return forwardDist * downwardDist
        }
    }
}