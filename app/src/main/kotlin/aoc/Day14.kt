package aoc

class Day14 {
    companion object {
        private fun readInput(input: List<String>,
                              rules: HashMap<Pair<Char, Char>, Char>,
                              count: HashMap<Pair<Char, Char>, Long>): Char {
            val inputIt = input.iterator()
            val chemical = inputIt.next()
            inputIt.next() // blank line

            while (inputIt.hasNext()) {
                val (left, right) = inputIt.next().split(" -> ")
                rules[Pair(left[0], left[1])] = right[0]
            }

            for (i in 1 until chemical.length) {
                val pair = Pair(chemical[i-1], chemical[i])
                count[pair] = count.getOrDefault(pair, 0) + 1
            }
            return chemical[0]
        }

        private fun polymerize(initial: HashMap<Pair<Char, Char>, Long>,
                               rules: HashMap<Pair<Char, Char>, Char>): HashMap<Pair<Char, Char>, Long> {
            val next = HashMap<Pair<Char, Char>, Long>()

            for ((pair, freq) in initial) {
                val newElement = rules[pair]!!
                val left = Pair(pair.first, newElement)
                val right = Pair(newElement, pair.second)
                next[left] = next.getOrDefault(left, 0) + freq
                next[right] = next.getOrDefault(right, 0) + freq
            }

            return next
        }

        private fun frequencyDifference(count: HashMap<Pair<Char, Char>, Long>, firstElement: Char): Long {
            val freqs = HashMap<Char, Long>()
            for ((pair, freq) in count) {
                freqs[pair.second] = freqs.getOrDefault(pair.second, 0) + freq
            }
            freqs[firstElement] = freqs.getOrDefault(firstElement, 0) + 1

            return (freqs.maxByOrNull { it.value }?.value ?: 0) - (freqs.minByOrNull { it.value }?.value ?: 0)
        }

        fun part1(input: List<String>): Long {
            val rules = HashMap<Pair<Char, Char>, Char>()
            var count = HashMap<Pair<Char, Char>, Long>()
            val first = readInput(input, rules, count)

            for (i in 0 until 10) {
                count = polymerize(count, rules)
            }

            return frequencyDifference(count, first)
        }

        fun part2(input: List<String>): Long {
            val rules = HashMap<Pair<Char, Char>, Char>()
            var count = HashMap<Pair<Char, Char>, Long>()
            val first = readInput(input, rules, count)

            for (i in 0 until 40) {
                count = polymerize(count, rules)
            }

            return frequencyDifference(count, first)
        }
    }
}