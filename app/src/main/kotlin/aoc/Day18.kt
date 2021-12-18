package aoc

import java.lang.Integer.max
import kotlin.math.ceil

class Day18 {
    companion object {
        private class NodeWrapper(var node: Node) {
            override fun toString(): String {
                return "$node"
            }

            fun canExplode(height: Int): Boolean {
                val pairNode = node as? PairNode ?: return false

                var can = false

                if ((pairNode.left)?.canExplode(height+1) == true) can = true
                if (height >= 4) {
                    explodeList.add(this)
                    can = true
                }
                if ((pairNode.right)?.canExplode(height+1) == true) can = true

                return can
            }

            fun explode() {
                val pairNode = node as? PairNode ?: throw Error("Can't explode literal")

                val leftLit = pairNode.left?.node as? LiteralNode
                val rightLit = pairNode.right?.node as? LiteralNode

                if (leftLit == null || rightLit == null) throw Error("No literals to explode")

                val newNode = LiteralNode(0)

                leftLit.leftLiteral?.let { it.value += leftLit.value }

                rightLit.rightLiteral?.let { it.value += rightLit.value }

                node = newNode
            }

            fun canSplit(): Boolean {
                val pairNode = node as? PairNode
                if (pairNode == null && (node as LiteralNode).value >= 10) {
                    splitList.add(this)
                    return true
                }

                var can = false

                if ((pairNode?.left)?.canSplit() == true) can = true
                if ((pairNode?.right)?.canSplit() == true) can = true

                return can
            }

            fun split() {
                val litNode = node as? LiteralNode ?: throw Error("Can't split pair")

                val newNode = PairNode()
                newNode.left = NodeWrapper(LiteralNode(litNode.value/2))
                newNode.right = NodeWrapper(LiteralNode(ceil(litNode.value/2.0).toInt()))
                node = newNode
            }

            fun magnitude(): Int {
                val pairNode = node as? PairNode ?: return (node as LiteralNode).value
                return 3 * pairNode.left!!.magnitude() + 2 * pairNode.right!!.magnitude()
            }
        }

        private abstract class Node

        private class PairNode(var left: NodeWrapper? = null,
                               var right: NodeWrapper? = null): Node() {
            override fun toString(): String {
                return "[$left,$right]"
            }
        }

        private class LiteralNode(var value: Int,
                                  var leftLiteral: LiteralNode? = null,
                                  var rightLiteral: LiteralNode? = null): Node() {
            override fun toString(): String {
                return "$value"
            }
        }

        private fun makeStringStream(input: String) = sequence {
            for (char in input) {
                yield(char)
            }
        }

        private lateinit var stringStream: Iterator<Char>

        private fun makeTree(): NodeWrapper {
            do {
                val char = stringStream.next()
                if (char == '[') {
                    val left = makeTree()
                    val right = makeTree()
                    return NodeWrapper(PairNode(left, right))
                } else if (char.isDigit()) {
                    var intString = char.toString()
                    var intChar = stringStream.next()
                    while (intChar.isDigit()) {
                        intString += intChar
                        intChar = stringStream.next()
                    }
                    return NodeWrapper(LiteralNode(intString.toInt()))
                }
            } while(true)
        }

        private fun setUpLinkedList(rootWrapper: NodeWrapper) {
            val leaves = ArrayList<LiteralNode>()

            fun recurseList(nodeWrapper: NodeWrapper) {
                val pairNode = nodeWrapper.node as? PairNode
                if (pairNode == null) {
                    leaves.add(nodeWrapper.node as LiteralNode)
                    return
                }

                pairNode.left?.also { recurseList(it) }
                pairNode.right?.also { recurseList(it) }
            }

            recurseList(rootWrapper)

            for ((i, leaf) in leaves.withIndex()) {
                if (i-1 >= 0) {
                    leaf.leftLiteral = leaves[i-1]
                }
                if (i+1 < leaves.size) {
                    leaf.rightLiteral = leaves[i+1]
                }
            }
        }

        private val explodeList = ArrayList<NodeWrapper>()
        private val splitList = ArrayList<NodeWrapper>()

        private fun reduceTree(root: NodeWrapper) {
            setUpLinkedList(root)

            while (true) {
                explodeList.clear()
                splitList.clear()
                val canExplode = root.canExplode(0)
                val canSplit = root.canSplit()

                if (canExplode) {
                    for (nodeWrapper in explodeList) {
                        nodeWrapper.explode()
                        setUpLinkedList(root)
                    }
                    continue
                } else if (canSplit) {
                    splitList.first().split()
                    setUpLinkedList(root)
                    continue
                }
                break
            }
        }

        fun part1(input: List<String>): Int {
            stringStream = makeStringStream(input.first()).iterator()
            var root = makeTree()
            for (line in input.drop(1)) {
                stringStream = makeStringStream(line).iterator()
                val node = makeTree()
                root = NodeWrapper(PairNode(root, node))
                reduceTree(root)
            }
            return root.magnitude()
        }
        fun part2(input: List<String>): Int {
            var maxMagnitude = 0
            for (i in input.indices) {
                for (j in input.indices) {
                    if (i == j) continue

                    stringStream = makeStringStream(input[i]).iterator()
                    val firstTree = makeTree()
                    stringStream = makeStringStream(input[j]).iterator()
                    val secondTree = makeTree()

                    val root = NodeWrapper(PairNode(firstTree, secondTree))
                    reduceTree(root)
                    maxMagnitude = max(maxMagnitude, root.magnitude())
                }
            }

            return maxMagnitude
        }

    }
}