package aoc

class Day02 {
    companion object {
        enum class Direction {
            forward, up, down
        }

        fun part1(input: List<String>): Int {
            var forwardDist = 0
            var downwardDist = 0

            for (line in input) {
                val (dir, dist) = line.split(" ")

                when (Direction.valueOf(dir)) {
                    Direction.forward -> forwardDist += dist.toInt()
                    Direction.up -> downwardDist -= dist.toInt()
                    Direction.down -> downwardDist += dist.toInt()
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
                    Direction.forward -> {
                        forwardDist += dist.toInt()
                        downwardDist += dist.toInt() * aim
                    }
                    Direction.up -> aim -= dist.toInt()
                    Direction.down -> aim += dist.toInt()
                }
            }
            return forwardDist * downwardDist
        }
    }
}