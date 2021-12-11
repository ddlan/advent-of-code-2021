package aoc

class Day08 {
    companion object {
        private fun processDisplay(signals: List<String>, displays: List<String>): List<Int> {
            val identities = MutableList(10) { "" }

            for (signal in signals) {
                when (signal.length) {
                    2 -> identities[1] = signal
                    3 -> identities[7] = signal
                    4 -> identities[4] = signal
                    7 -> identities[8] = signal
                }
            }

            // We know 1, 4, 7, 8
            val bd = identities[4].toSet() subtract identities[1].toSet()
            signals.filter { it.length == 5 && it.toSet().containsAll(bd) }.forEach { identities[5] = it }

            // We know 1, 4, 5, 7, 8
            val a = identities[7].toSet() - identities[1].toSet()
            var adg = identities[5].toSet()
            signals.filter { it.length == 5 }.forEach { adg = adg intersect it.toSet() }
            val d = (adg subtract a) intersect bd
            signals.filter { it.length == 6 && !it.toSet().containsAll(d) }.forEach { identities[0] = it }

            // We know 0, 1, 4, 5, 7, 8
            var acdg = signals.first { it.length == 5 && it != identities[5] }.toSet()
            signals.filter { it.length == 5 && it != identities[5] }.forEach { acdg = acdg intersect it.toSet() }
            val c = acdg subtract adg
            signals.filter { it.length == 6 && !it.toSet().containsAll(c) }.forEach { identities[6] = it }

            // We know 0, 1, 4, 5, 6, 7, 8
            signals.filter { it.length == 6 && !identities.contains(it) }.forEach { identities[9] = it }

            // We know 0, 1, 4, 5, 6, 7, 8, 9
            val e = identities[8].toSet() subtract identities[9].toSet()
            signals.filter { it.length == 5 && it.toSet().containsAll(e) }.forEach { identities[2] = it }
            signals.filter { it.length == 5 && !identities.contains(it) }.forEach { identities[3] = it }

            val identitySet = identities.map { it.toSet() }

            val output = ArrayList<Int>()
            for (display in displays) {
                if (display.isEmpty()) continue // skip whitespace
                output.add(identitySet.indexOf(display.toSet()))
            }
            return output
        }

        fun part1(input: List<String>): Int {
            var numOf1478 = 0

            for (line in input) {
                val (signalString, displayString) = line.split("|")
                val signals = signalString.split(" ")
                val displays = displayString.split(" ")

                // drop first display which is a whitespace
                val nums = processDisplay(signals, displays)
                for (num in nums) {
                    when (num) {
                        1 -> numOf1478++
                        4-> numOf1478++
                        7 -> numOf1478++
                        8 -> numOf1478++
                        else -> {}
                    }
                }
            }

            return numOf1478
        }

        fun part2(input: List<String>): Int {
            var total = 0

            for (line in input) {
                val (signalString, displayString) = line.split("|")
                val signals = signalString.split(" ")
                val displays = displayString.split(" ")

                val nums = processDisplay(signals, displays)
                var curr = 0
                for (num in nums) {
                    curr *= 10
                    curr += num
                }
                total += curr
            }

            return total
        }
    }
}
