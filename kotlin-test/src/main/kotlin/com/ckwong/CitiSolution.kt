package com.ckwong

import kotlin.math.min

class CitiSolution {

    fun solution(S: String, C: IntArray): Int {
        var lastChar: Char = S[0]
        var maxCost = C[0]
        var sumCost = C[0]
        var sameCount = 1
        var total: Int = 0
        fun reset(i: Int) {
            val c = C[i]
            lastChar = S[i]
            maxCost = c
            sumCost = c
            sameCount = 1
        }
        fun update(i: Int) {
            val c = C[i]
            if (c > maxCost) {
                maxCost = c
            }
            sumCost += c
            sameCount++
        }
        fun calc() {
            if (sameCount > 1) {
                total += (sumCost - maxCost)
            }
        }
        (1 until S.length).forEach { i ->
            val c = S[i]
            if (c == lastChar) {
                update(i)
            } else {
                calc()
                reset(i)
            }
        }
        calc()
        return total
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = CitiSolution()
            println(sol.solution("aaabbcc", intArrayOf(1, 2, 0, 1, 2, 3, 4)))
            println(sol.solution("abcb", intArrayOf(1, 2, 3, 4)))
        }
    }
}