package aoc

class Day23 {
    companion object {
        private data class Diagram(
            val hallway: MutableList<Char>,
            val rooms: MutableList<MutableList<Char>>
        ) {
            fun clone(): Diagram {
                return Diagram(ArrayList(hallway), rooms.map { ArrayList(it) }.toMutableList())
            }

            override fun toString(): String {
                return "$hallway $rooms"
            }

            fun isValid(): Boolean {
                if (hallway.filter { it == '.' }.size != hallway.size) return false
                val letters = listOf('A', 'B', 'C', 'D')

                for (i in 0 until 4) {
                    if (rooms[i].filter { it == letters[i] }.size != rooms[i].size) return false
                }

                return true
            }
        }

        private fun readInput(input: List<String>): Diagram {
            val hallway = input[1].slice(1 until 12).toMutableList()
            val rooms = (0 until 4).map { ArrayList<Char>().toMutableList() }.toMutableList()

            for (i in 2 until 4) {
                val line = input[i]
                for (j in 0 until 4) {
                    rooms[j].add(line[j*2+3])
                }
            }

            return Diagram(hallway, rooms)
        }

        private val dp = HashMap<Diagram, Int>()

        private val charCosts = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
        private val charRooms = mapOf('A' to 0, 'B' to 1, 'C' to 2, 'D' to 3)
        private val hallRoom = mapOf(2 to 0, 4 to 1, 6 to 2, 8 to 3)
        private val roomHall = listOf(2, 4, 6, 8)

        private fun solve(diagram: Diagram, cost: Int, visited: Map<Diagram, Int>): Int {
            if (diagram.isValid()) {
                dp[diagram] = cost
                return cost
            }

            // hallway
            for (i in 0 until 11) {
                val char = diagram.hallway[i]
                if (char != '.') {
                    if (i-1 >= 0 && diagram.hallway[i-1] == '.') {
                        val newDiagram = diagram.clone()
                        newDiagram.hallway[i-1] = char
                        newDiagram.hallway[i] = '.'
                        if (visited.getOrDefault(newDiagram, Int.MAX_VALUE) <= cost) {
                            return -1
                        }
                    }
                }
            }

            return -1
        }

        fun part1(input: List<String>): Int {
            val diagram = readInput(input)
            return solve(diagram, 0, HashMap())
        }

        fun part2(input: List<String>): Int {
            return input.size
        }
    }
}