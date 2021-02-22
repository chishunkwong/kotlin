package com.ckwong

class LinesInPlaneSolution {

    fun maxPoints(points: Array<IntArray>): Int {
        return 0
    }

    /**
     * Captures what a line is, xDiff and yDiff together defines the slope.
     * Note for our purpose they are integers, which of course in general they may not be.
     * The lines are the same if they have the same slope and same y-intercept.
     * In the special case of a vertical line, yIntercept will be the x-intercept.
     *
     * We need this class so we can use it as keys in a hashmap
     */
    data class Line(val xDiff: Int, val yDiff: Int, val yIntercept: Double) {
        override fun equals(other: Any?): Boolean {
            if (other is Line) {
                return other.yIntercept == this.yIntercept && other.xDiff * this.yDiff == other.yDiff * this.xDiff
            }
            return false
        }

        override fun hashCode(): Int {
            return if (xDiff == 0) {
                yIntercept.toInt()
            } else {
                yDiff / xDiff + yIntercept.toInt()
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
                        intArrayOf(1, 1),
                        intArrayOf(2, 2),
                        intArrayOf(100, 100),
                        intArrayOf(100000000, 100000001),
                        intArrayOf(2, 1),
                        intArrayOf(1, 0),
                        intArrayOf(0, 1)
                    ).toTypedArray()
                )
            )
        }
    }
}