package com.ckwong

import java.lang.IllegalArgumentException
import java.util.Deque
import java.util.LinkedList

class NestedIterator(nestedList: List<NestedInteger>) {
    val nl = nestedList
    // which nested integer is up next
    var listIndex = -1
    // the current nested integer, already flattened
    // the nested lists, the last one is the one we get the next value from
    var flattened: Deque<Int>? = null

    init {
        getNextFlattened()
    }

    fun next(): Int {
        if (flattened == null)
            throw IllegalArgumentException("No next element")
        val nextNi = flattened!!
        val answer = nextNi.removeFirst()
        if (nextNi.isEmpty()) {
            getNextFlattened()
        }
        return answer
    }

    private fun getNextFlattened() {
        while ((flattened == null || flattened!!.isEmpty()) && ++listIndex < nl.size) {
            flattened = flatten(nl[listIndex])
        }
        if (flattened != null && flattened.isNullOrEmpty()) {
            flattened = null
        }
    }

    fun hasNext(): Boolean {
        return flattened != null
    }

    private fun flatten(ni: NestedInteger, list: Deque<Int> = LinkedList()): Deque<Int> {
        if (ni.isInteger()) {
            list.add(ni.getInteger())
        } else {
            val niList = ni.getList()!!
            niList.forEach {
                flatten(it, list)
            }
        }
        return list
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
            // val iter = NestedIterator(listOf(NestedInteger()))
            var counter = 0
            while (iter.hasNext() && counter++ < 100) {
                print("${iter.next()}, ")
            }
            println("")
        }
    }
}