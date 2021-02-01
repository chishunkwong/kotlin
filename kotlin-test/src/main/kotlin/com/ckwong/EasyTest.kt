package com.ckwong

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.sqrt

fun main(args: Array<String>) {
    // sumArray()
    // posNegZero()
    // bigFactorial(args[0].toInt())
    /*
    println(
        subArray(
            (0..4).toList().map { args[it] }.toTypedArray(),
            (5..6).toList().map { args[it] }.toTypedArray()
        )
    )
     */
    // sumNoAdd(-7, -40)
    // println(sumNoAddUsingMod(2147483647, -2147483648) - 2147483647 - -2147483648)
    /*
    fourSumCount(
        listOf(1, 2).toIntArray(),
        listOf(-2, -1).toIntArray(),
        listOf(2, -1).toIntArray(),
        listOf(2, 0).toIntArray()
    )
    averageWaitingTime(listOf<IntArray>(
        intArrayOf(5, 2),
        intArrayOf(5, 4),
        intArrayOf(10, 3),
        intArrayOf(20, 1),
    ).toTypedArray())
     */
    // largestNumber(intArrayOf(3, 30, 34, 5, 9))
    // largestNumber(intArrayOf(0, 0))
    // numSquares(88)
    // countSubstrings("aaa")
    // subarraySum(intArrayOf(1, 2, 3, 4), 3)
    // println(wordBreak("catsanddog", listOf("cats", "dog", "sand", "and", "cat")))
    /*
    println(wordBreak("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        listOf("aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa","ba")))
     */
    // merge(listOf(intArrayOf(1, 3), intArrayOf(2, 6), intArrayOf(8, 10), intArrayOf(15, 18)).toTypedArray())
    countSmaller(intArrayOf(2, 1, 6, 4, 0, 2, 8))
}

/**
 * Given an array of integers, return an array of integers where each number represents
 * the count of numbers to the right that are smaller than the number at that index.
 * I.e. in a line of boys, count the ones ahead of you that are shorter than you are.
 */
fun countSmaller(nums: IntArray): List<Int> {
    val sorted = nums.mapIndexed { index, value -> Triple(index, value, AtomicInteger(0)) }
        .sortedWith { a, b ->
            val order = a.second - b.second
            // println("$a, $b, $order")
            if (order < 0 && a.first < b.first) {
                // println("inc b")
                b.third.set(b.third.get() + 1)
            }
            if (order > 0 && a.first > b.first) {
                // println("inc a")
                a.third.set(b.third.get() + 1)
            }
            -1 * order
        }
    println(sorted)
    val answer = (0 until nums.size).toList().toIntArray()
    var accu = 0
    sorted.reversed().forEachIndexed { index, triple ->
        answer[triple.first] = index - triple.third.get() - accu
        accu += triple.third.get()
        println(accu)
    }
    println(answer.toList())
    return answer.toList()
}

/**
 * Merge possibly overlapping intervals into non-overlapping ones.
 * The intervals are assumed to be closed on both ends.
 */
fun merge(intervals: Array<IntArray>): Array<IntArray> {
    if (intervals.isEmpty()) return intervals
    // sort by starts
    intervals.sortBy { it[0] }
    val answer = mutableListOf<IntArray>()
    var curStart = intervals[0][0]
    var curEnd = intervals[0][1]
    intervals.forEach {
        val thisStart = it[0]
        val thisEnd = it[1]
        if (thisStart == curStart) {
            if (thisEnd > curEnd) {
                curEnd = thisEnd
            }
            // else nothing to do, the current interval is inside the existing one
        } else {
            if (thisStart > curEnd) {
                // we have a completed, merged, interval
                answer.add(intArrayOf(curStart, curEnd))
                curStart = thisStart
                curEnd = thisEnd
            } else if (thisEnd > curEnd) {
                curEnd = thisEnd
            }
            // else nothing to do, the current interval is inside the existing one
        }
    }
    // add the last one
    answer.add(intArrayOf(curStart, curEnd))
    answer.forEach { print("${it.toList()} ") }
    return answer.toTypedArray()
}

/**
 * check whether a string can be split into words using the given words
 */
fun wordBreak(s: String, wordDict: List<String>): Boolean {
    val need = s.toCharArray().toSet()
    val has = wordDict.map {
        it.toCharArray().toList()
    }.flatten().toSet()
    if ((need - has).isNotEmpty()) {
        println("returning early, missing chars so no way we can")
        return false
    }
    return wordBreakDictSorted(s, wordDict.sorted().reversed(), mutableSetOf())
}

fun wordBreakDictSorted(s: String, wordDict: List<String>, noCantDo: MutableSet<String>): Boolean {
    if (s.isBlank()) return true
    if (noCantDo.contains(s)) return false
    println("$s, $wordDict")
    wordDict.forEach {
        if (s.startsWith(it)) {
            val maybe = wordBreakDictSorted(s.substring(it.length), wordDict, noCantDo)
            if (maybe) return true
        }
    }
    noCantDo.add(s)
    return false
}

/**
 * this one is still not fast enough, the fastest answer is O(n), and is done by simply adding from 0 to i,
 * and then remembering how many subarrays add to a particular sum, now sum(i, j) = sum(0, j) - sum(0, i-1),
 * so we look up by the difference that we need and see if some subarrays can provide that, and if so, how many.
 * (I am not going to code this as I will just be copying
 * https://leetcode.com/problems/subarray-sum-equals-k/discuss/1023453/Java-Optimal-Solution-with-Visuals)
 */
fun subarraySum(nums: IntArray, k: Int): Int {
    val sumsByIndices = mutableMapOf<Pair<Int, Int>, Int>()
    val len = nums.size
    var answer = 0
    (0 until len).forEach { i ->
        (i until len).forEach { j ->
            val ij = if (i == j) {
                nums[i]
            } else {
                nums[j] + sumsByIndices[Pair(i, j - 1)]!!
            }
            if (ij == k) answer++
            sumsByIndices[Pair(i, j)] = ij
        }
    }
    println(answer)
    return answer
}

/**
 * Same as above, but this one is not effective, failed time limit test
 */
fun subarraySum2(nums: IntArray, k: Int): Int {
    val len = nums.size
    val answer = (0 until len).map {
        initSubarraySum(nums.sliceArray(it until len), k)
    }.sum()
    println("answer $answer")
    return answer
}

fun initSubarraySum(nums: IntArray, k: Int, emptyIsZero: Boolean = false): Int {
    val len = nums.size
    val matched = if (k == 0 && emptyIsZero) 1 else 0
    return if (len == 0) {
        matched
    } else {
        matched + initSubarraySum(nums.sliceArray(1 until len), k - nums[0], true)
    }
}

/**
 * Count the number of substrings that are palindromes
 * Same substring in two different positions count as two
 * Single character substrings count as palindromes
 */
fun countSubstrings(s: String): Int {
    val len = s.length
    if (len == 0) return 0
    if (len == 1) return 1
    val numAtChar0 = (1..len).map {
        if (isPalindrome(s.substring(0, it))) 1 else 0
    }.sum()
    val answer = numAtChar0 + countSubstrings(s.substring(1))
    println(answer)
    return answer
}

fun isPalindrome(s: String): Boolean {
    if (s.isBlank()) return false
    val len = s.length
    (0 until len / 2).forEach {
        if (s[it] != s[len - it - 1]) return false
    }
    return true
}

/**
 * Number of perfect squares needed to get a sum of a given n
 * Recursion with memorization,
 * Alternatively, use some math theorem, which then can greatly speed it up but is not the point
 * (the theorem is called Legendre 3-Square Theorem)
 */
fun numSquares(n: Int): Int {
    if (n <= 0) return 0
    // number to minimal number of perfect squares needed
    val known = mutableMapOf<Int, Int>()
    val answer = minNumSquares(n, known)
    println("answer $answer $known")
    return answer
}

fun minNumSquares(n: Int, known: MutableMap<Int, Int>): Int {
    val existing = known[n]
    if (existing != null) return existing
    if (n <= 3) {
        known[n] = n
        return n
    }
    val root = floor(sqrt(n.toDouble())).toInt()
    if (root * root == n) {
        known[n] = 1
        return 1
    }
    (1..root).forEach {
        val challenger = 1 + minNumSquares(n - it * it, known)
        val existing = known[n]
        if (existing == null || existing > challenger) {
            known[n] = challenger
        }
    }
    return known[n]!!
}

fun largestNumber(nums: IntArray): String {
    val sorted = nums.sortedWith { a, b ->
        val ab = BigInteger(a.toString() + b.toString())
        val ba = BigInteger(b.toString() + a.toString())
        when {
            ab > ba -> -1
            ab < ba -> 1
            else -> 0
        }
    }
    val builder = StringBuilder()
    sorted.forEach {
        builder.append(it)
    }
    val answer = builder.toString()
    val nonZeros = answer.find {
        it != '0'
    }
    println(answer)
    return if (nonZeros != null) answer else "0"
}

fun averageWaitingTime(customers: Array<IntArray>): Double {
    var actualStart = customers[0][0]
    val answer = customers.map {
        // perhaps the customer walked in and no outstanding order was being made
        actualStart = maxOf(it[0], actualStart)
        val waitTime = actualStart + it[1] - it[0]
        actualStart += it[1]
        waitTime
    }.average()
    println(answer)
    return answer
}

fun fourSumCount(A: IntArray, B: IntArray, C: IntArray, D: IntArray): Int {
    // val answer = sumMatch(0, A, B, C, D)
    if (A.isEmpty()) return 0
    val aAndB = sumAndFrequency(A, B)
    val cAndD = sumAndFrequency(C, D)
    val aAndBSums = aAndB.keys.toList().sorted()
    val cAndDSums = cAndD.keys.toList().sortedDescending()
    // edge cases, may make it faster
    if (aAndBSums.first()!! + cAndDSums.last()!! > 0) return 0
    if (aAndBSums.last()!! + cAndDSums.first()!! < 0) return 0
    var answer = 0
    var cAndDIndex = 0
    val len = cAndDSums.size
    aAndBSums.forEach { ab ->
        // println("$ab, $cAndDIndex")
        while (ab + cAndDSums[cAndDIndex] > 0) {
            cAndDIndex++
            if (cAndDIndex >= len) {
                return answer
            }
        }
        val cd = cAndDSums[cAndDIndex]
        if (ab + cd == 0) {
            answer += (aAndB[ab]!! * cAndD[cd]!!)
        }
    }
    println(aAndBSums)
    println(cAndDSums)
    println(answer)
    return answer
}

/**
 * Given two arrays of integers, calculate the sums by taking one element from each array,
 * then count the frequencies of such sums (E.g. if 1 + 1 = 2 and -1 + 3 = 2 then 2 has a frequency of 2)
 */
fun sumAndFrequency(A: IntArray, B: IntArray): Map<Int, Int> {
    return A.map { a ->
        B.map { b ->
            a + b
        }
    }.flatten().groupingBy {
        it
    }.eachCount()
}

/**
 * recursive solution to fourSumCount, works but slow O(n^4)
 */
fun sumMatch(sum: Int, vararg arrays: IntArray): Int {
    val firstArr = arrays[0]
    if (arrays.size == 1) {
        return firstArr.filter {
            it == sum
        }.count()
    }
    val remaining = arrays.copyOfRange(1, arrays.size)
    // println("$sum, ${firstArr.toList()}")
    return firstArr.map { numInArr ->
        sumMatch(sum - numInArr, *remaining)
    }.sum()
}

/**
 * Compute the sum of two integers without using the + or - operators
 */
fun sumNoAdd(a: Int, b: Int) {
    fun getCrement(isAdd: Boolean): (Int) -> Int {
        fun crement(cur: Int): Int {
            var sum = cur
            return if (isAdd) ++sum else --sum
        }
        return ::crement
    }

    val crement = getCrement(b >= 0)
    var sum = a
    (0 until b.absoluteValue).forEach {
        sum = crement(sum)
    }
    println(sum)
}

fun sumNoAddUsingMod(a: Int, b: Int): Int {
    if (a == 0) {
        return b
    }
    if (b == 0) {
        return a
    }
    if (b.absoluteValue < a.absoluteValue) {
        // because we mod by a, so a bit faster is we use the one with a smaller absolute value
        return sumNoAddUsingMod(b, a)
    }
    val bIsNegative = b < 0
    val a1 = if (bIsNegative) -a else a
    val b1 = if (bIsNegative) -b else b
    var factor = b1 / a1
    val factorDouble = (b1 + 0.0) / a1
    val doOvershoot = (factorDouble - floor(factorDouble)) < 0.5
    val mod = b1 % a1
    var modPositive = if (mod > 0) mod else (mod + a1)
    if (mod > 0) {
        factor++
    }
    if (doOvershoot) {
        factor++
        modPositive = a1 - modPositive
    }
    var sum = a1 * factor
    // println(sum)
    // println(b1 % a1)
    (0 until modPositive).forEach {
        if (doOvershoot) {
            sum--
        } else {
            sum++
        }
    }
    sum = if (bIsNegative) -sum else sum
    return sum
}


fun subArray(arr: Array<String>, sub: Array<String>): Boolean {
    println(arr.toList())
    println(sub.toList())
    // sanity check
    val arrLen = arr.size
    val subLen = sub.size
    if (arrLen < subLen) throw IllegalArgumentException("length")
    if (arr.map { it.length }.distinct().size != 1) throw IllegalArgumentException("non-uniform width on outer array")
    if (sub.map { it.length }.distinct().size != 1) throw IllegalArgumentException("non-uniform width on inner array")
    if (sub.isEmpty()) return true
    val arrWidth = arr[0].length
    val subWidth = sub[0].length
    if (arrWidth < subWidth) throw IllegalArgumentException("width")

    arr.forEachIndexed { index, s ->
        // went past the point that can match, because
        // there are not enough lines in the outer array to fit the inner array
        if (index + subLen > arrLen) {
            println("short circuit outer $index")
            return false
        }
        var startIndex = 0
        while (startIndex >= 0) {
            var firstLineMatchedIndex = s.indexOf(sub[0], startIndex)
            // there can be a second match on the same line, so need to walk the line from this first found index
            if (firstLineMatchedIndex >= 0) {
                var matched = true
                (1 until subLen).let rangeLoop@{ range ->
                    range.forEach {
                        if (arr[index + it].substring(
                                firstLineMatchedIndex,
                                firstLineMatchedIndex + subWidth
                            ) != sub[it]
                        ) {
                            println("short circuit inner ${sub[it]}")
                            matched = false
                            // short circuit, need the artificial lambda with .let so we can return for that lambda,
                            // hence exiting the loop
                            return@rangeLoop
                        }
                    }
                }
                if (matched) {
                    println("$s, $firstLineMatchedIndex")
                    return true
                }
                startIndex = firstLineMatchedIndex + 1
            } else {
                startIndex = -1
            }
        }
    }
    return false
}

fun bigFactorial(n: Int) {
    val factorial = when {
        n < 0 -> "no factorial for negative number"
        n < 1 -> 1
        n < 15 ->
            (1..n).reduce { acc, i ->
                acc * i
            }
        else ->
            (1..n).fold(BigInteger.valueOf(1)) { acc, i ->
                acc.multiply(BigInteger.valueOf(i.toLong()))
            }
    }
    println(factorial)
}

fun posNegZero() {
    // the most unreadable way to do the job
    val arr = listOf(1, -1, -2, 0, 4, -111, -5, 23, 0, 0, -999999).toIntArray()
    val initial = Triple(0, 0, 0)
    fun reducer(cur: Triple<Int, Int, Int>, item: Int) =
        when {
            // first is numPos, second is numNeg, third is numZero
            item > 0 -> Triple(cur.first + 1, cur.second, cur.third)
            item < 0 -> Triple(cur.first, cur.second + 1, cur.third)
            else -> Triple(cur.first, cur.second, cur.third + 1)
        }

    val result = arr.fold(initial, ::reducer)
    println(result)
}

fun posNegZero2() {
    // just the most ineffective way to do the job
    val arr = listOf(1, -1, -2, 0, 4, -111, -5, 23, 0, 0, -999999).toIntArray()
    val num = arr.size + 0.0
    val numPos = arr.filter { it > 0 }.size
    val numNeg = arr.filter { it < 0 }.size
    val numZeros = arr.filter { it == 0 }.size
    println("Pos: ${numPos / num}, neg: ${numNeg / num}, zeros: ${numZeros / num}")
}

fun sumArray() {
    val arr = listOf(1, 1, 2, 3, 5).toIntArray()
    var sum = 0
    arr.forEach { sum += it }
    println("$sum")
}
