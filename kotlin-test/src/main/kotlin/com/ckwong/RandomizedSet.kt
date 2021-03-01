package com.ckwong

class RandomizedSet {

    private val map = mutableMapOf<Int, Int>()
    private val list = mutableListOf<Int>()

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    fun insert(`val`: Int): Boolean {
        val existing = map[`val`]
        return if (existing == null) {
            map[`val`] = list.size
            list.add(`val`)
            true
        } else {
            false
        }
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    fun remove(`val`: Int): Boolean {
        val existing = map.remove(`val`)
        val foo = if (existing != null) {
            if (existing < list.size - 1) {
                val last = list.removeLast()
                map[last] = existing
                list[existing] = last
            } else {
                // we happen to be removing the last element in the list, so no need to swap anything
                list.removeLast()
            }
            true
        } else {
            false
        }
        println(list)
        return foo
    }

    /** Get a random element from the set. */
    fun getRandom(): Int {
        println(list)
        return list[(0 until list.size).random()]
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val set = RandomizedSet()
            println(set.insert(0))
            println(set.insert(1))
            println(set.remove(0))
            println(set.insert(2))
            println(set.remove(1))
            println(set.getRandom())
        }
    }
}