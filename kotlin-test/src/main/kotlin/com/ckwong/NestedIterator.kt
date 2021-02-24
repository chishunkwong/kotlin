package com.ckwong

class NestedIterator(val nestedList: List<NestedInteger>) {

    var pointer: Int = 0

    fun next(): NestedInteger {
        return nestedList[pointer]
    }

    fun hasNext(): Boolean {
        val next = nestedList[pointer]
        return next != null
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