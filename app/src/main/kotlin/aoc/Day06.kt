package aoc

class Day06 {
    companion object {
        private fun totalLanternFish(startingFishes: List<Long>, days: Int): Long {
            var fishes = startingFishes

            for (i in 0 until days) {
                val nextFishes = MutableList<Long>(9) { 0 }
                for ((index, count) in fishes.withIndex()) {
                    if (index == 0) {
                        nextFishes[6] += count
                        nextFishes[8] += count
                    } else {
                        nextFishes[index-1] += count
                    }
                }
                fishes = nextFishes
            }

            return fishes.sum()
        }

        fun part1(input: List<String>): Long {
            val fishes = MutableList<Long>(9) { 0 }
            input[0].split(",").forEach { fishes[it.toInt()]++ }
            return totalLanternFish(fishes, 80)
        }

        fun part2(input: List<String>): Long {
            val fishes = MutableList<Long>(9) { 0 }
            input[0].split(",").forEach { fishes[it.toInt()]++ }
            return totalLanternFish(fishes, 256)
        }
    }
}
