package aoc

import kotlin.math.abs
import kotlin.math.max

class Day19 {
    companion object {
        private data class Coord(val x: Int, val y: Int, val z: Int) {
            override fun toString(): String {
                return "($x, $y, $z)"
            }

            fun plus(other: Coord): Coord {
                return Coord(x + other.x, y + other.y, z + other.z)
            }

            fun minus(other: Coord): Coord {
                return Coord(x - other.x, y - other.y, z - other.z)
            }

            fun manhattanDistance(other: Coord): Int {
                return abs(x-other.x) + abs(y-other.y) + abs(z-other.z)
            }
        }

        private var pOrientations: List<List<List<Int>>>? = null
        private val orientations: List<List<List<Int>>>
            get() {
                if (pOrientations == null) {
                    val orientations = ArrayList<List<List<Int>>>()
                    for (x in 0 until 6) {
                        val (xPos, xMag) = listOf(x % 3, x % 2)
                        val xOrient = arrayListOf(0, 0, 0)
                        xOrient[xPos] = if (xMag == 1) 1 else -1
                        for (y in 0 until 6) {
                            val (yPos, yMag) = listOf(y % 3, y % 2)
                            if (yPos == xPos) continue
                            val yOrient = arrayListOf(0, 0, 0)
                            yOrient[yPos] = if (yMag == 1) 1 else -1
                            for (z in 0 until 6) {
                                val (zPos, zMag) = listOf(z % 3, z % 2)
                                if (zPos == xPos || zPos == yPos) continue
                                val zOrient = arrayListOf(0, 0, 0)
                                zOrient[zPos] = if (zMag == 1) 1 else -1
                                orientations.add(listOf(xOrient, yOrient, zOrient))
                            }
                        }
                    }
                    pOrientations = orientations
                }

                return pOrientations as List<List<List<Int>>>
            }

        private fun readScanners(input: List<String>): List<Set<Coord>> {
            val scanners = ArrayList<Set<Coord>>()

            var scanner: HashSet<Coord>? = null
            for (line in input) {
                if (line.isEmpty()) {
                    continue
                } else if (line.contains("scanner")) {
                    if (scanner != null) scanners.add(scanner)
                    scanner = HashSet()
                } else {
                    val (x, y, z) = line.split(',').map { it.toInt() }
                    scanner?.add(Coord(x, y, z))
                }
            }
            if (scanner != null) scanners.add(scanner)

            return scanners
        }

        // returns a rotation and an offset to go from other -> rotated -> scanner
        private fun overlay(scanner: Set<Coord>, otherScanner: Set<Coord>): Pair<Int, Coord>? {
            for (orientation in orientations.indices) {
                val rotatedScanner = otherScanner.map { rotate(it, orientation) }.toSet()

                for (rotatedCoord in rotatedScanner) {
                    for (coord in scanner) {
                        val offset = coord.minus(rotatedCoord)
                        var count = 0
                        for (currRotatedCoord in rotatedScanner) {
                            for (currCoord in scanner) {
                                val currOffset = currCoord.minus(currRotatedCoord)
                                if (offset == currOffset) count++
                                if (count >= 12) {
                                    return Pair(orientation, offset)
                                }
                            }
                        }


                    }
                }
            }

            return null
        }

        private fun rotate(coord: Coord, orientation: Int): Coord {
            val xRotation = orientations[orientation][0].map { it * coord.x }
            val yRotation = orientations[orientation][1].map { it * coord.y }
            val zRotation = orientations[orientation][2].map { it * coord.z }
            val fullRotation = (0 until 3).map { xRotation[it] + yRotation[it] + zRotation[it]  }

            return Coord(fullRotation[0], fullRotation[1], fullRotation[2])
        }

        private val scannerCoords = HashSet<Coord>(listOf(Coord(0, 0 ,0)))

        fun part1(input: List<String>): Int {
            val scanners = readScanners(input)
            val uncountedScanners = (1 until scanners.size).toMutableList()
            val allScanners = HashSet<Coord>(scanners[0])

            while (uncountedScanners.isNotEmpty()) {
                var newIndex: Int? = null

                for (uncountedIndex in uncountedScanners) {
                    val result = overlay(allScanners, scanners[uncountedIndex])
                    if (result != null) {
                        val (orientation, offset) = result
                        val transformedCoords = scanners[uncountedIndex].map { rotate(it, orientation).plus(offset) }
                        allScanners.addAll(transformedCoords)
                        scannerCoords.add(offset)

                        newIndex = uncountedIndex
                        break
                    }
                }

                if (newIndex != null) {
                    uncountedScanners.remove(newIndex)
                }
            }

            return allScanners.count()
        }

        fun part2(input: List<String>): Int {
            var maxDist = 0
            for (scannerCoord in scannerCoords) {
                for (other in scannerCoords) {
                    maxDist = max(maxDist, scannerCoord.manhattanDistance(other))
                }
            }

            return maxDist
        }
    }
}