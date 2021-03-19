package com.ckwong

import kotlin.math.min

class CitiSolution {

    fun solution(S: String, C: IntArray): Int {
        var lastChar: Char = S[0]
        var minCost = C[0]
        var sumCost = C[0]
        var sameCount = 1
        var total: Int = 0
        (1 until S.length).forEach { i ->
            val c = S[i]
            val cost = C[i]
            if (c == lastChar) {
                if (cost < minCost) {
                    minCost = cost
                }
                sumCost += cost
                sameCount++
            } else {
                if (sameCount > 1) {
                    total += (sumCost - minCost)
                }
                lastChar = S[i]
                minCost = cost
                sumCost = cost
                sameCount = 1
            }
        }
        if (sameCount > 1) {
            total += (sumCost - minCost)
        }
        return total
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = CitiSolution()
            print(sol.solution("aaabbcc", intArrayOf(1, 2, 0, 1, 2, 1, 2)))
        }
    }
}