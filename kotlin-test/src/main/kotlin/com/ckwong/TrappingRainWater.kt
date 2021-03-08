package com.ckwong

class TrappingRainWater {

    fun trap(height: IntArray): Int {
        if (height.size < 3) return 0
        var total = 0
        // infinite loop escape latch
        var counter = 0
        do {
            val filled = fillLocalMinima(height)
            // println("$filled, ${height.toList()}")
            total += filled
        } while (filled > 0 && counter++ < 100000)
        return total
    }

    /**
     * Walking from left to right, find any local minima and fill it up to the next lowest level.
     * The height array will be modified as the local minima get filled
     * @return the total amount of water filled
     */
    private fun fillLocalMinima(height: IntArray): Int {
        var toLeftIndex: Int? = null
        var toLeftHeight: Int? = null
        var lastHeight = height[0]
        var currIndex = 1
        var totalFilled = 0
        // infinite loop escape latch
        var counter = 0
        while (currIndex < height.size && counter++ < 100000) {
            val currHeight = height[currIndex]
            when {
                currHeight == lastHeight -> {
                    // we are going flat, nothing to do except advancing currIndex, which we do at the bottom
                }
                currHeight < lastHeight -> {
                    // we are going down, so replace the left with the new one
                    toLeftIndex = currIndex - 1
                    toLeftHeight = lastHeight
                    lastHeight = currHeight
                }
                (currHeight > lastHeight && toLeftIndex == null) -> {
                    // We are going up, but we are at the beginning, so no minima for us, yet
                    lastHeight = currHeight
                }
                (currHeight > lastHeight && toLeftIndex != null) -> {
                    // we found a local minima
                    val thisTroveHeight = if (currHeight > toLeftHeight!!) toLeftHeight else currHeight
                    totalFilled += (currIndex - toLeftIndex - 1) * (thisTroveHeight - lastHeight)
                    lastHeight = currHeight
                    // Fill the hole, so the landscape has one fewer local minima
                    (toLeftIndex + 1 until currIndex).forEach {
                        height[it] = thisTroveHeight
                    }
                    // once we fill a local minima, we pretend to be at the start and not worry about further filling,
                    // let the recursive calls to this function take care of that
                    // (I suppose we can keep toLeftHeight/Index as a stack and pop them, will see)
                    toLeftHeight = null
                    toLeftIndex = null
                }
                // no need for else as we are not using "when" as an expression, and there is no else anyway
            }
            currIndex++
        }
        return totalFilled
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val solution = TrappingRainWater()
            println(solution.trap(intArrayOf(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1))) // expects 6
        }
    }
}