package aoc

class Day10 {
    companion object {
        fun part1(input: List<String>): Int {
            var score = 0
            val stack = ArrayDeque<Char>()

            for (line in input) {
                for (char in line) {
                    when (char) {
                        '(', '[', '{', '<' -> stack.add(char)
                        ')' -> if (stack.removeLast() != '(') { score += 3; break }
                        ']' -> if (stack.removeLast() != '[') { score += 57; break }
                        '}' -> if (stack.removeLast() != '{') { score += 1197; break }
                        '>' -> if (stack.removeLast() != '<') { score += 25137; break }
                    }
                }
            }

            return score
        }

        fun part2(input: List<String>): Long {
            val scores = ArrayList<Long>()

            for (line in input) {
                val stack = ArrayDeque<Char>()
                var corrupt = false
                for (char in line) {
                    when (char) {
                        '(', '[', '{', '<' -> stack.add(char)
                        ')' -> if (stack.removeLast() != '(') { corrupt = true; break }
                        ']' -> if (stack.removeLast() != '[') { corrupt = true; break }
                        '}' -> if (stack.removeLast() != '{') { corrupt = true; break }
                        '>' -> if (stack.removeLast() != '<') { corrupt = true; break }
                    }
                }
                if (!corrupt) {
                    val rightStack = ArrayDeque<Char>()
                    var score = 0L
                    while (!stack.isEmpty()) {
                        when (val char = stack.removeLast()) {
                            ')', ']', '}', '>' -> rightStack.add(char)
                            '(' -> if (rightStack.removeLastOrNull() == null) { score *= 5; score += 1 }
                            '[' -> if (rightStack.removeLastOrNull() == null) { score *= 5; score += 2 }
                            '{' -> if (rightStack.removeLastOrNull() == null) { score *= 5; score += 3 }
                            '<' -> if (rightStack.removeLastOrNull() == null) { score *= 5; score += 4 }
                        }
                    }
                    scores.add(score)
                }
            }

            return scores.sorted()[scores.size/2]
        }
    }
}
