package com.ckwong


/**
 * See the other class of the same name without the "2"
 *
 * That other class has a workable solution, but slow, this one implements Khan's algorithm
 * (and I didn't come up with it independently),
 * the essence is the same, but the clever part is to remove the dependency once a course is added,
 * so we don't have to keep using set inclusion to check but rather just check if any dependencies are left.
 */
class ClassScheduleSolution2 {
    fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
        val courses = (0 until numCourses).map {
            Course(it)
        }
        prerequisites.forEach { prereq ->
            val len = prereq.size
            (0 until len - 1).forEach {
                // it depends on it+1
                courses[prereq[it]]!!.dependencies.add(prereq[it + 1])
                courses[prereq[it + 1]]!!.neededBy.add(prereq[it])
            }
        }
        val coursesSet = courses.map { it.num }.toMutableSet()
        val answer = mutableListOf<Int>()
        var lastRoundAdded = true
        var counter = 0
        while (coursesSet.isNotEmpty() && lastRoundAdded && counter++ < 10000) {
            lastRoundAdded = false
            val toRemove = mutableSetOf<Int>()
            coursesSet.forEach {
                val course = courses[it]
                if (course.dependencies.isEmpty()) {
                    answer.add(it)
                    toRemove.add(it)
                    course.neededBy.forEach { dependent ->
                        courses[dependent].dependencies.remove(it)
                    }
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

    private data class Course(
        val num: Int,
        val dependencies: MutableSet<Int> = mutableSetOf(),
        val neededBy: MutableSet<Int> = mutableSetOf()
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = ClassScheduleSolution2()
            println(
                sol.findOrder(
                    4,
                    listOf(
                        intArrayOf(1, 0), intArrayOf(2, 0), intArrayOf(3, 1), intArrayOf(3, 2)
                    ).toTypedArray()
                ).toList()
            )
        }
    }
}