package aoc

import java.util.*
import kotlin.Comparator

class Day15 {
    companion object {
        private fun makeGrid(input: List<String>): List<List<Int>> {
            return input.map { line -> line.map { Character.getNumericValue(it) } }
        }

        private fun adjacentSpaces(y: Int, x: Int, grid: List<List<Int>>): List<Pair<Int, Int>> {
            val (height, width) = listOf(grid.size, grid[0].size)

            return arrayListOf(Pair(y-1, x), Pair(y, x-1), Pair(y+1, x), Pair(y, x+1)).filter(fun(pair): Boolean {
                val (ay, ax) = pair
                return ay in 0 until height && ax in 0 until width
            })
        }

        private fun dijkstra(grid: List<List<Int>>): Int {
            val (height, width) = listOf(grid.size, grid[0].size)

            val dist = grid.map { it.map{ Int.MAX_VALUE }.toMutableList() }.toMutableList()
            val visited = grid.map { it.map{ false }.toMutableList() }.toMutableList()
            dist[0][0] = 0

            val compare: Comparator<Pair<Int, Int>> = compareBy { dist[it.first][it.second] }
            val pQueue = PriorityQueue(compare)
            pQueue.add(Pair(0,0))

            while (pQueue.isNotEmpty()) {
                val (y, x) = pQueue.remove()
                if (visited[y][x]) continue
                if (y == height-1 && x == width-1) return dist[y][x]
                visited[y][x] = true

                for ((ay, ax) in adjacentSpaces(y, x, grid)) {
                    if (visited[ay][ax]) continue
                    val newCost = dist[y][x] + grid[ay][ax]
                    if (newCost < dist[ay][ax]) {
                        dist[ay][ax] = newCost
                        pQueue.add(Pair(ay, ax))
                    }
                }
            }

            return -1
        }

        fun part1(input: List<String>): Int {
            return dijkstra(makeGrid(input))
        }

        private fun makeFatGrid(input: List<String>): List<List<Int>> {
            val grid = makeGrid(input)
            val newGrid = ArrayList<ArrayList<Int>>()

            for (a in 0 until 5) {
                for (line in grid) {
                    val currLine = ArrayList<Int>()
                    for (b in 0 until 5) {
                        val newLine = line.map { val new = (it + a + b); if (new > 9) (new % 10 + 1) else new }
                        currLine += newLine
                    }
                    newGrid.add(currLine)
                }
            }

            return newGrid
        }

        fun part2(input: List<String>): Int {
            return dijkstra(makeFatGrid(input))
        }
    }
}
