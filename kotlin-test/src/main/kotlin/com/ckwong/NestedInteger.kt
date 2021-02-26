package com.ckwong

class NestedInteger {

    var i: Int? = null
    var l: MutableList<NestedInteger>? = null

    constructor() {
        l = mutableListOf()
    }

    // Constructor initializes a single integer.
    constructor(value: Int) {
        i = value
    }

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    fun isInteger(): Boolean {
        return i != null
    }

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    fun getInteger(): Int? {
        return i
    }

    // Set this NestedInteger to hold a single integer.
    fun setInteger(value: Int) {
        i = value
        l = null
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    fun add(ni: NestedInteger): NestedInteger {
        i = null
        if (l == null) {
            l = mutableListOf(ni)
        } else {
            l!!.add(ni)
        }
        return this
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    fun getList(): List<NestedInteger>? {
        return l
    }

    override fun toString(): String {
        if (i != null) return i.toString()
        if (l != null) return "a list of length ${l!!.size}"
        return "internal error"
    }
}