package aoc

import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Day12 {
    companion object {
        private fun isLowercase(str: String): Boolean {
            return str == str.lowercase()
        }
        
        private fun makeGraph(input: List<String>): Map<String, List<String>> {
            val graph = HashMap<String, ArrayList<String>>()

            input.forEach { line ->
                val (a, b) = line.split('-')
                graph.putIfAbsent(a, ArrayList())
                graph[a]?.add(b)
                graph.putIfAbsent(b, ArrayList())
                graph[b]?.add(a)
            }

            return graph
        }

        private fun dfs1(node: String, graph: Map<String, List<String>>, visited: HashSet<String>): Int {
            if (node == "end") return 1

            var pathCount = 0
            graph[node]?.also { children ->
                for (child in children) {
                    if (visited.contains(child)) continue

                    if (isLowercase(child)) visited.add(child)
                    pathCount += dfs1(child, graph, visited)
                    if (isLowercase(child)) visited.remove(child)
                }
            }

            return pathCount
        }

        fun part1(input: List<String>): Int {
            val graph = makeGraph(input)
            return dfs1("start", graph, hashSetOf("start"))
        }

        private fun dfs2(node: String, graph: Map<String, List<String>>, visited: HashSet<String>, hasRevisited: Boolean): Int {
            if (node == "end") return 1

            var pathCount = 0
            graph[node]?.also { children ->
                for (child in children) {
                    if (child == "start") continue

                    if (visited.contains(child)) {
                        if (hasRevisited) continue

                        pathCount += dfs2(child, graph, visited, true)
                    } else {
                        if (isLowercase(child)) visited.add(child)
                        pathCount += dfs2(child, graph, visited, hasRevisited)
                        if (isLowercase(child)) visited.remove(child)
                    }
                }
            }

            return pathCount
        }

        fun part2(input: List<String>): Int {
            val graph = makeGraph(input)
            return dfs2("start", graph, hashSetOf("start"), false)
        }
    }
}