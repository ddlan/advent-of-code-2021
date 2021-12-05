package aoc

class Day03 {
    companion object {
        enum class Comparison {
            greater, less
        }

        private fun binStringToInt(str: String): Int {
            var ans = 0
            var bitMask = 1

            for (i in str.length-1 downTo 0) {
                if (str[i] == '1') {
                    ans += bitMask
                }
                bitMask = bitMask shl 1
            }

            return ans
        }

        fun part1(input: List<String>): Int {
            val lineCount = input.size
            val posCounts = IntArray(input[0].length)

            for (line in input) {
                for ((i, ch) in line.withIndex()) {
                    if (ch == '1') {
                        posCounts[i]++
                    }
                }
            }

            var gamma = ""
            var epsilon = ""

            for (posCount in posCounts) {
                if (posCount > lineCount/2) {
                    gamma += '1'
                    epsilon += '0'
                } else {
                    gamma += '0'
                    epsilon += '1'
                }
            }

            val gammaVal = binStringToInt(gamma)
            val epsilonVal = binStringToInt(epsilon)

            return gammaVal * epsilonVal
        }

        private fun filter(input: List<String>, comparison: Comparison): Int {
            val validAns: MutableList<String> = MutableList(input.size) { index -> input[index] }

            for (i in 0 until validAns[0].length) {
                var oneCount = 0
                for (ans in validAns) {
                    if (ans[i] == '1') {
                        oneCount++
                    }
                }

                val zeroCount = validAns.size - oneCount
                val chars = if (comparison == Comparison.greater) { "10" } else { "01" }
                val keepPredicate: Char = if (oneCount >= zeroCount) { chars[0] } else { chars[1] }

                validAns.retainAll { str -> str[i] == keepPredicate }

                if (validAns.size == 1) {
                    return binStringToInt(validAns[0])
                }
            }

            throw Exception("Did not complete filter! $validAns")
        }

        fun part2(input: List<String>): Int {
            val oxygen = filter(input, Comparison.greater)
            val scrubber = filter(input, Comparison.less)

            return oxygen * scrubber
        }
    }
}
