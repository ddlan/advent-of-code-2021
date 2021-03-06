package aoc

class Day01 {
    companion object {
        fun part1(input: List<String>): Int {
            var count = 1
            
            for (i in 1 until input.size) {
                if (input[i] > input[i-1]) {
                    count++
                }
            }

            return count
        }

        fun part2(input: List<String>): Int {
            val intPut = input.map{ it.toInt() }

            var count = 0
            var windowOne = intPut.slice(0..2).sum()
            var windowTwo = intPut.slice(1..3).sum()

            if (windowTwo > windowOne) {
                count++
            }

            for (i in 3..input.size-2) {
                windowOne = windowOne - intPut[i-3] + intPut[i]
                windowTwo = windowTwo - intPut[i-2] + intPut[i+1]
                
                if (windowTwo > windowOne) {
                    count++
                }
            }

            return count
        }
    }
}