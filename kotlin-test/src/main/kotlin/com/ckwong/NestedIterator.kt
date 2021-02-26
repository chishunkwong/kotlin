package com.ckwong

import java.util.Deque
import java.util.LinkedList

class NestedIterator(val nestedList: List<NestedInteger>) {

    var listIndex = 0
    // the nested lists, the last one is the one we get the next value from
    var nested: Deque<NestedInteger> = LinkedList()
    // the indices for getting to the nested lists above, the last value if the index of the next value
    var pointers: Deque<Int> = LinkedList()
    var nextInt: Int? = 0

    init {
        nextInt = nextInternal()
    }

    private fun nextInternal(): Int? {
        if (nestedList.size <= listIndex) return null
        val i = nextInNestedInteger()
        return if (i == null) {
            listIndex++
            nested = LinkedList()
            pointers = LinkedList()
            if (nestedList.size <= listIndex) {
                null
            } else {
                nextInNestedInteger()
            }
        } else {
            i
        }
    }

    private fun nextInNestedInteger(): Int? {
        val ni = if (nested.isEmpty()) {
            // start from the top
            nestedList[listIndex]
        } else {
            nested.last()
        }
        return if (ni.isInteger()) {
            advancePointers()
            ni.getInteger()
        } else {
            val ni2 = ni.getList()!![pointers.last()]
            if (ni2.isInteger()) {
                advancePointers()
                ni2.getInteger()
            } else {
                pointers.push(0)
                nested.push(ni2)
                nextInNestedInteger()
            }
        }
    }

    private fun advancePointers() {
        val lastNi = nested.last()
        if (lastNi == null) {

        }
    }

    fun next(): Int {
        val answer = nextInt!!
        // proactively get the next one
        nextInt = nextInternal()
        return answer
    }

    fun hasNext(): Boolean {
        return nextInt != null
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val one = NestedInteger(1)
            val twoThree = NestedInteger()
            val four = NestedInteger(4)
            val five = NestedInteger(5)
            twoThree.add(NestedInteger(2)).add(NestedInteger(3))
            val iter = NestedIterator(listOf(one, twoThree, NestedInteger().add(NestedInteger().add(four)).add(five)))
            var counter = 0
            while (iter.hasNext() && counter++ < 100) {
                println(iter.next())
            }
        }
    }
}