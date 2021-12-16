package aoc

class Day14 {
    companion object {
        private fun readInput(input: List<String>, rules: HashMap<Pair<Char, Char>, Char>): String {
            val inputIt = input.iterator()
            val initial = inputIt.next()
            inputIt.next() // blank line

            while (inputIt.hasNext()) {
                val (left, right) = inputIt.next().split(" -> ")
                rules[Pair(left[0], left[1])] = right[0]
            }
            return initial
        }

        private fun polymerize(initial: String, rules: HashMap<Pair<Char, Char>, Char>): String {
            var additions = ""
            for (i in 1 until initial.length) {
                additions += rules[Pair(initial[i-1], initial[i])]
            }

            return initial.dropLast(1).zip(additions) { a, b ->
                listOf(a, b)
            }.flatten().joinToString("") + initial[initial.lastIndex]
        }

        private fun frequencyDifference(chemical: String): Int {
            val count = HashMap<Char, Int>()

            for (element in chemical) {
                count[element] = count.getOrDefault(element, 0) + 1
            }

            return (count.maxByOrNull { it.value }?.value ?: 0) - (count.minByOrNull { it.value }?.value ?: 0)
        }

        fun part1(input: List<String>): Int {
            val rules = HashMap<Pair<Char, Char>, Char>()
            var curr = readInput(input, rules)

            for (i in 0 until 10) {
                curr = polymerize(curr, rules)
            }

            return frequencyDifference(curr)
        }

//        private fun polymerizeCount(initial: HashMap<Pair<Char, Char>, Int>,
//                                    rules: HashMap<Pair<Char, Char>, Char>): HashMap<Pair<Char, Char>, Int> {
//            return null
//        }

        fun part2(input: List<String>): Int {
            val rules = HashMap<Pair<Char, Char>, Char>()
            val chemical = readInput(input, rules)
            var curr = HashMap<Pair<Char, Char>, Int>()

            for (i in 1 until chemical.length) {
                val pair = Pair(chemical[i-1], chemical[i])
                curr[pair] = curr.getOrDefault(pair, 0) + 1
            }

//            for (i in 0 until 40) {
//                curr = polymerizeCount(curr, rules)
//            }

            return 0//frequencyDifference(curr)
        }
    }
}