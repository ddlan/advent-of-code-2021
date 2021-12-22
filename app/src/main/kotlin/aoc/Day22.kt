package aoc

import kotlin.math.max
import kotlin.math.min

class Day22 {
    companion object {
        private data class Reboot(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
            override fun toString(): String {
                return "[(${x.first}, ${x.last}), (${y.first}, ${y.last}), (${z.first}, ${z.last})]"
            }
        }

        private fun readInput(input: List<String>): List<Reboot> {
            return input.map { line ->
                val lineList = line.split("..", " x=", ",y=", ",z=")
                val on = lineList[0] == "on"
                val x = lineList[1].toInt()..lineList[2].toInt()
                val y = lineList[3].toInt()..lineList[4].toInt()
                val z = lineList[5].toInt()..lineList[6].toInt()

                Reboot(on, x, y, z)
            }
        }

        fun part1(input: List<String>): Int {
            val reboots = readInput(input)

            val cubes = (0..100).map { (0..100).map { (0..100).map { false }.toMutableList() } }
            val offset = -50

            for (reboot in reboots) {
                if (reboot.x.first < -50 || reboot.x.last > 50
                    || reboot.y.first < -50 || reboot.y.last > 50
                    || reboot.z.first < -50 || reboot.z.last > 50 ) {
                    continue
                }

                for (cx in reboot.x) {
                    for (cy in reboot.y) {
                        for (cz in reboot.z) {
                            cubes[cx-offset][cy-offset][cz-offset] = reboot.on
                        }
                    }
                }
            }

            return cubes.fold(0) { pacc, plane ->
                plane.fold(pacc) { lacc, line ->
                    line.fold(lacc) { cacc, cube -> cacc + if (cube) 1 else 0}
                }
            }
        }

        private fun intersect(cube: Reboot, other: Reboot): Reboot? {
            if (cube.x.first > other.x.last || cube.x.last < other.x.first) return null
            if (cube.y.first > other.y.last || cube.y.last < other.y.first) return null
            if (cube.z.first > other.z.last || cube.z.last < other.z.first) return null
            return Reboot(
                true,
                max(cube.x.first, other.x.first)..min(cube.x.last, other.x.last),
                max(cube.y.first, other.y.first)..min(cube.y.last, other.y.last),
                max(cube.z.first, other.z.first)..min(cube.z.last, other.z.last),
            )
        }

        private fun diff(cube: Reboot, intersection: Reboot): List<Reboot> {
            val newCubes = ArrayList<Reboot>()

            newCubes.add(Reboot(true, cube.x, cube.y, cube.z.first..intersection.z.first-1))
            newCubes.add(Reboot(true, cube.x, cube.y, intersection.z.last+1..cube.z.last))
            newCubes.add(Reboot(true, cube.x.first..intersection.x.first-1, cube.y, intersection.z))
            newCubes.add(Reboot(true, intersection.x.last+1..cube.x.last, cube.y, intersection.z))
            newCubes.add(Reboot(true, intersection.x, cube.y.first..intersection.y.first-1, intersection.z))
            newCubes.add(Reboot(true, intersection.x, intersection.y.last+1..cube.y.last, intersection.z))

            return newCubes.filter { it.x.first <= it.x.last && it.y.first <= it.y.last && it.z.first <= it.z.last }
        }

        fun part2(input: List<String>): Long {
            val reboots = readInput(input)

            var cubes = ArrayList<Reboot>()
            for (reboot in reboots) {
                val newCubes = ArrayList<Reboot>()
                for (cube in cubes) {
                    val intersection = intersect(cube, reboot)
                    if (intersection == null) {
                        newCubes.add(cube)
                    } else {
                        newCubes.addAll(diff(cube, intersection))
                    }
                }
                if (reboot.on) newCubes.add(reboot)
                cubes = newCubes
            }

            return cubes.fold(0L) { acc, reboot ->
                acc + ((reboot.x.last - reboot.x.first + 1).toLong()
                        * (reboot.y.last - reboot.y.first + 1).toLong()
                        * (reboot.z.last - reboot.z.first + 1).toLong())
            }
        }
    }
}