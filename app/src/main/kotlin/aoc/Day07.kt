package aoc

import kotlin.math.abs
import kotlin.math.roundToInt

class Day07 {
    companion object {
        fun part1(input: List<String>): Int {
            val crabs: ArrayList<Int> = input[0].split(",").map { it.toInt() } as ArrayList<Int>

            val sortedCrabs = crabs.sorted()
            val median: Double =
                if (sortedCrabs.size % 2 == 0)
                    (sortedCrabs[sortedCrabs.size/2].toDouble() + sortedCrabs[sortedCrabs.size/2 - 1].toDouble()) / 2
                else
                    sortedCrabs[sortedCrabs.size/2].toDouble()
            val medianInt = median.roundToInt()

            // add 0 to list so accumulator starts at 0
            crabs.add(0, 0)

            return crabs.reduce { acc, crab -> acc + abs(crab - medianInt) }
        }

        private fun stepCost(step: Int): Int {
            if (step == 0) return 0
            return (1..step).reduce { acc, i -> acc + i }
        }

        fun part2(input: List<String>): Int {
            val crabs: ArrayList<Int> = input[0].split(",").map { it.toInt() } as ArrayList<Int>

            val start = crabs.minOrNull()
            val end = crabs.maxOrNull()

            // add 0 to list so accumulator starts at 0
            crabs.add(0, 0)

            var minCost = Int.MAX_VALUE
            for (i in start!!..end!!) {
                val currCost = crabs.reduce { acc, crab -> acc + stepCost(abs(crab - i)) }
                if (currCost < minCost) {
                    minCost = currCost
                }
            }

            return minCost
        }
    }
}
