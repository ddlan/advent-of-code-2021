package aoc

class Day16 {
    companion object {
        private fun makeBitSequence(hexString: String) = sequence {
            for (digit in hexString) {
                val hex = Integer.parseInt(digit.toString(), 16)
                for (mask in listOf(8, 4, 2, 1)) {
                    yield((hex and mask) != 0)
                }
            }
        }

        private lateinit var bitIterator: Iterator<Boolean>
        private var currBit = 0
        private var versionSum = 0L

        private fun nextNBits(n: Int): Long {
            currBit += n
            var num = 0L
            for (i in 0 until n) {
                val x = bitIterator.next()
                num =  num * 2 + if (x) 1 else 0
            }
            return num
        }

        private fun processLiteral(): Long {
            var literal = 0L
            do {
                val continueFlag = nextNBits(1) == 1L
                literal = literal*16 + nextNBits(4)
            }
            while(continueFlag)
            return literal
        }

        private fun processSubpackets(): List<Long> {
            val subpackets = ArrayList<Long>()

            val opId = nextNBits(1)
            if (opId == 0L) {
                val bitCount = nextNBits(15)
                val startBit = currBit
                while(currBit < startBit + bitCount) {
                    subpackets.add(processPackets())
                }
            } else {
                val subpacketCount = nextNBits(11)
                for (i in 0 until subpacketCount) {
                    subpackets.add(processPackets())
                }
            }

            return subpackets
        }

        private fun processPackets(): Long {
            val version = nextNBits(3)
            versionSum += version
            val type = nextNBits(3)

            return if (type == 4L) { // literal
                processLiteral()
            } else { // operator
                val subpackets = processSubpackets()
                when (type) {
                    0L -> subpackets.sum()
                    1L -> subpackets.reduce { acc, it -> acc * it }
                    2L -> subpackets.minOrNull()!!
                    3L -> subpackets.maxOrNull()!!
                    5L -> if (subpackets[0] > subpackets[1]) 1 else 0
                    6L -> if (subpackets[0] < subpackets[1]) 1 else 0
                    else -> if (subpackets[0] == subpackets[1]) 1 else 0
                }
            }
        }

        fun part1(input: List<String>): Long {
            bitIterator = makeBitSequence(input.first()).iterator()
            processPackets()
            return versionSum
        }

        fun part2(input: List<String>): Long {
            bitIterator = makeBitSequence(input.first()).iterator()
            return processPackets()
        }
    }
}