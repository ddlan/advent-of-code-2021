package aoc

import kotlin.math.max

class Day21 {
    companion object {
        // store positions as 0-9 for easy modulo math
        private class Game1(p1Pos: Int, p2Pos: Int) {
            var p1Turn = true
            val posns = arrayListOf(p1Pos, p2Pos)
            val scores = arrayListOf(0,0)
            val spaceCount = 10
            val winCondition = 1000
            var dieRollCount = 0

            fun dice() = sequence {
                while (true) {
                    for (i in 1..100) {
                        dieRollCount++
                        yield(i)
                    }
                }
            }

            val currDice = dice().iterator()

            fun play(): Int {
                while (true) {
                    val (activePlayer, otherPlayer) = if (p1Turn) listOf(0, 1) else listOf(1, 0)

                    val roll = currDice.next() + currDice.next() + currDice.next()
                    posns[activePlayer] = (posns[activePlayer] + roll) % spaceCount
                    scores[activePlayer] += posns[activePlayer] + 1

                    if (scores[activePlayer] >= winCondition) {
                        return scores[otherPlayer] * dieRollCount
                    }

                    p1Turn = !p1Turn
                }
            }
        }

        fun part1(input: List<String>): Int {
            val (p1Pos, p2Pos) = input.map{ it.split(' ').last().toInt()}
            val game = Game1(p1Pos-1, p2Pos-1)
            return game.play()
        }

        data class State(val p1Turn: Boolean, val p1Pos: Int, val p2Pos: Int, val p1Score: Int, val p2Score: Int)

        // store positions as 0-9 for easy modulo math
        private class Game2 {
            val spaceCount = 10
            val winCondition = 21
            val dp = HashMap<State, Pair<Long, Long>>()

            fun dice() = sequence {
                for (i in 1..3) {
                    for (j in 1..3) {
                        for (k in 1..3) {
                            yield(i+j+k)
                        }
                    }
                }
            }

            fun play(state: State): Pair<Long, Long> {
                val prev = dp[state]
                if (prev != null) return prev

                var (p1Wins, p2Wins) = listOf(0L, 0L)
                val activePos = if (state.p1Turn) state.p1Pos else state.p2Pos
                val activeScore = if (state.p1Turn) state.p1Score else state.p2Score

                val currDice = dice().iterator()
                while (currDice.hasNext()) {
                    val currActivePos = (activePos + currDice.next()) % spaceCount
                    val currActiveScore = activeScore + currActivePos + 1

                    if (currActiveScore >= winCondition) {
                        if (state.p1Turn) p1Wins++ else p2Wins++
                    } else {
                        val newState = State(
                            !state.p1Turn,
                            if (state.p1Turn) currActivePos else state.p1Pos,
                            if (!state.p1Turn) currActivePos else state.p2Pos,
                            if (state.p1Turn) currActiveScore else state.p1Score,
                            if (!state.p1Turn) currActiveScore else state.p2Score)

                        val (subP1Wins, subP2Wins) = play(newState)
                        p1Wins += subP1Wins
                        p2Wins += subP2Wins
                    }
                }

                val ans = Pair(p1Wins, p2Wins)
                dp[state] = ans
                return ans
            }
        }

        fun part2(input: List<String>): Long {
            val (p1Pos, p2Pos) = input.map{ it.split(' ').last().toInt()}
            val game = Game2()
            val (p1Wins, p2Wins) = game.play(State(true, p1Pos-1, p2Pos-1, 0, 0))
//            val(p1Wins, p2Wins)= game.play2(Pair(Pair(p1Pos, 0), Pair(p2Pos, 0)), true)
//            println("universes ${game.universes}")
            return max(p1Wins, p2Wins)
        }
    }
}