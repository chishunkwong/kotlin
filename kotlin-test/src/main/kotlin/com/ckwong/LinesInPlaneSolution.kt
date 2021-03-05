package com.ckwong

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.round
import kotlin.math.sqrt

class LinesInPlaneSolution {

    fun maxPoints(points: Array<IntArray>): Int {
        val pointsWithCount = mutableMapOf<Pair<Int, Int>, AtomicInteger>()
        points.forEach {
            val p = Pair(it[0], it[1])
            val existing = pointsWithCount[p]
            if (existing != null) {
                existing.incrementAndGet()
            } else {
                pointsWithCount[p] = AtomicInteger(1)
            }
        }
        val lines = mutableMapOf<Line, Pair<AtomicInteger, AtomicInteger>>()
        val distinctPoints = pointsWithCount.keys.toList()
        val len = distinctPoints.size
        if (len == 0) return 0
        // There can be many points that are in the same spot
        if (len == 1) return points.size
        (0 until len).forEach { i ->
            // complete loop through twice, let the double count be, we will divide by 2 later
            (0 until len).forEach { j ->
                if (i != j) {
                    val p1 = distinctPoints[i]
                    val p2 = distinctPoints[j]
                    val sumContribution = pointsWithCount[p1]!!.get() + pointsWithCount[p2]!!.get()
                    if (p1.first == p1.second && p2.first == p2.second)
                        println("$p1, $p2, $sumContribution")
                    val line = Line(p1, p2)
                    val existing = lines[line]
                    if (existing != null) {
                        // first tracks the total instance of points sitting on this line
                        // second tracks the number of distinct points sitting on this line
                        existing.first.addAndGet(sumContribution)
                        existing.second.addAndGet(1)
                    } else {
                        lines[line] = Pair(AtomicInteger(sumContribution), AtomicInteger(1))
                    }
                }
            }
        }
        val answer = lines.values.map {
            // solve the quadratic equation n(n+1) = distinct points duplicate count,
            // because each distinct point contributed to the line n times,
            // once with each of the other point on the line
            val dupCount = (round(sqrt(4 * it.second.get() + 1.0)).toInt() - 1) / 2
            // multiply by 2 because the occurrence counts are also doubled
            it.first.get() / (2 * dupCount)
        }.maxOrNull()!!
        return answer
    }

    /**
     * Captures what a line is, xDiff and yDiff together defines the slope.
     * Note for our purpose they are integers, which of course in general they may not be.
     * The lines are the same if they have the same slope and same y-intercept.
     * In the special case of a vertical line, yIntercept will be the x-intercept.
     *
     * We need this class so we can use it as keys in a hashmap
     */
    data class Line private constructor(
        val p1: Pair<Int, Int>, val p2: Pair<Int, Int>,
        val xDiff: Int, val yDiff: Int,
        val yInterceptNom: Int, val yInterceptDenom: Int
    ) {

        init {
            val foo: Int = 0
            if (xDiff == 0 && yDiff == 0)
                throw IllegalArgumentException("The two points are the same, cannot define a line")
            if (xDiff == 0 && yInterceptDenom != 1)
                throw IllegalArgumentException(
                    "For a vertical line y-intercept denominator must be 1" +
                            " and y-intercept nominator will be the x-intercept"
                )
        }

        constructor(p1: Pair<Int, Int>, p2: Pair<Int, Int>)
                : this(
            p1, p2,
            p1.first - p2.first, p1.second - p2.second,
            if (p1.first == p2.first) {
                // vertical line
                p1.first
            } else {
                p1.second * (p1.first - p2.first) - p1.first * (p1.second - p2.second)
            },
            if (p1.first == p2.first) {
                // vertical line
                1
            } else {
                p1.first - p2.first
            }
        )

        override fun equals(other: Any?): Boolean {
            if (other is Line) {
                return other.yInterceptNom * this.yInterceptDenom == other.yInterceptDenom * this.yInterceptNom
                        && other.xDiff * this.yDiff == other.yDiff * this.xDiff
            }
            return false
        }

        override fun hashCode(): Int {
            return if (xDiff == 0) {
                yInterceptNom
            } else {
                yDiff / xDiff + yInterceptNom / yInterceptDenom
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = LinesInPlaneSolution()
            println(
                sol.maxPoints(
                    listOf(
                        intArrayOf(0, 0),
                        intArrayOf(0, 0),
                        intArrayOf(1, 1),
                        intArrayOf(2, 2),
                        intArrayOf(5, 5),
                        intArrayOf(100, 100),
                        intArrayOf(100000000, 100000001),
                        intArrayOf(2, 1),
                        intArrayOf(2, 9),
                        intArrayOf(2, 9),
                        intArrayOf(2, 9),
                        intArrayOf(1, 9),
                        intArrayOf(1, 9),
                        intArrayOf(1, 9),
                        intArrayOf(0, 1)
                    ).toTypedArray()
                )
            )
        }
    }
}