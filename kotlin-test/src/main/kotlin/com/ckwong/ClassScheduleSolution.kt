package com.ckwong

import kotlin.reflect.jvm.internal.impl.descriptors.impl.ModuleDependencies

/**
 * https://leetcode.com/problems/course-schedule-ii/
 *
 * There are a total of n courses you have to take labelled from 0 to n - 1.
 * Some courses may have prerequisites, for example, if prerequisites[i] = [ai, bi] this means you must take the course bi before the course ai.
 * Given the total number of courses numCourses and a list of the prerequisite pairs, return the ordering of courses you should take to finish all courses.
 * If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.
 */
class ClassScheduleSolution {
    fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
        val courses = (0 until numCourses).map {
            it to Course(it)
        }.toMap()
        prerequisites.forEach { prereq ->
            val len = prereq.size
            (0 until len - 1).forEach {
                // it depends on it+1
                courses[prereq[it]]!!.dependencies.add(prereq[it + 1])
            }
        }
        val coursesSet = courses.values.map { it.num }.toMutableSet()
        val answer = mutableListOf<Int>()
        var lastRoundAdded = true
        var counter = 0
        while (coursesSet.isNotEmpty() && lastRoundAdded && counter++ < 10000) {
            lastRoundAdded = false
            val toRemove = mutableSetOf<Int>()
            coursesSet.forEach {
                val alreadyHave = answer.toSet()
                if (alreadyHave.containsAll(courses[it]!!.dependencies)) {
                    answer.add(it)
                    toRemove.add(it)
                    // println("toRemove $toRemove")
                    lastRoundAdded = true
                }
            }
            coursesSet.removeAll(toRemove)
        }
        return if (coursesSet.isNotEmpty()) {
            intArrayOf()
        } else {
            answer.toIntArray()
        }
    }

    private data class Course(val num: Int, val dependencies: MutableSet<Int> = mutableSetOf())

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = ClassScheduleSolution()
            println(sol.findOrder(4,
                listOf(
                    intArrayOf(1,0), intArrayOf(2,0), intArrayOf(3,1), intArrayOf(3,2)
                ).toTypedArray()).toList())
        }
    }
}