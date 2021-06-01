package com.ckwong

import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.floor
import kotlin.math.sqrt

class Solution {

    fun isValidSudoku(board: Array<CharArray>): Boolean {
        val indices = 0..8
        val rows = indices.map { board[it].toList() }
        val columns = indices.map { i -> board.map { it[i] } }
        val zeroOneTwo = 0..2
        val squares = zeroOneTwo.map { i ->
            zeroOneTwo.map { j ->
                zeroOneTwo.map { ii ->
                    zeroOneTwo.map { jj ->
                        board[i * 3 + ii][j * 3 + jj]
                    }
                }.flatten()
            }
        }.flatten()
        listOf(rows, columns, squares).forEach {
            it.forEach { theNine ->
                if (!checkOneToNine(theNine)) return false
            }
        }
        return true
    }

    private fun checkOneToNine(chars: List<Char>): Boolean {
        // chars are expected to be 1-9 or .
        chars.filter { it != '.' }.groupBy { it }.forEach {
            if (it.value.size > 1) return false
        }
        return true
    }

    /**
     * Given a linked list, remove the n-th node, counting from tail (n is 1-based, so if n = 1 then remove the tail)
     * The goal is to do it in one pass, otherwise, of course we can walk to the end,
     * count the steps along the way and then subtract by n and walk again
     */
    fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
        if (n < 1) return head
        if (head == null) return head
        if (head.next == null) return if (n == 1) null else head
        var nAhead: ListNode = head
        (1..n).forEach {
            val nextNode = nAhead.next
            if (nextNode == null) {
                // there are not enough nodes to go n steps ahead,
                // so there are not enough to walk n steps back from the tail either,
                // except the edge condition of n being the size of the list
                return if (it == n) {
                    head.next
                } else {
                    head
                }
            }
            nAhead = nextNode
        }
        var nextOneThenN = nAhead.next
        var nextOne: ListNode = head.next!! // trust me, we are not going to pull from rug from under you!
        var cur: ListNode = head
        while (nextOneThenN != null) {
            cur = nextOne
            // if nextOneThenN is not null, nextOne.next can't be null
            nextOne = nextOne.next!!
            nextOneThenN = nextOneThenN.next
        }
        cur.next = nextOne.next
        return head
    }

    /**
     * Calculate x^y by taking the square root of y, then recurse.
     * At first glance this may seem like the right solution, as it quickly
     * reduce the size of the power, but it actually ends up calling itelf more
     * times than if we just half y, then multiply by x one more time if y is odd.
     */
    fun myPowByRoot(x: Double, n: Int, counter: AtomicInteger): Double {
        counter.incrementAndGet()
        // stupid min value edge case
        if (n == Int.MIN_VALUE) {
            return 1 / (myPowByRoot(x, Int.MAX_VALUE, counter) * x)
        }
        if (n == 0) return 1.0
        if (n < 0) return 1 / myPowByRoot(x, -n, counter)
        if (n == 1) return x
        // The next two are optional really, should make it slightly faster
        if (n == 2) return x * x
        if (n == 3) return x * x * x
        val root = floor(sqrt(n.toDouble())).toInt()
        val rootPow = myPowByRoot(x, root, counter)
        return if (root * root == n) {
            myPowByRoot(rootPow, root, counter)
        } else {
            myPowByRoot(rootPow, root, counter) * myPowByRoot(x, n - root * root, counter)
        }
    }

    /**
     * Calculate x^y by taking half of y, then recuse. See similar function above for comparison
     */
    fun myPowByDivide(x: Double, n: Int, counter: AtomicInteger): Double {
        counter.incrementAndGet()
        // stupid min value edge case
        if (n == Int.MIN_VALUE) {
            return 1 / (myPowByDivide(x, Int.MAX_VALUE, counter) * x)
        }
        if (n == 0) return 1.0
        if (n < 0) return 1 / myPowByDivide(x, -n, counter)
        if (n == 1) return x
        val half = n / 2
        val halfPow = myPowByDivide(x, half, counter)
        return if (half * 2 == n) {
            halfPow * halfPow
        } else {
            halfPow * halfPow * x
        }
    }

    /**
     * Swap an array of 0s, 1s and 2s in-place.
     * I didn't know it but it is a well-known algo called Dutch National flag algorithm
     */
    fun sortColors(nums: IntArray): Unit {
        var lastZero: Int? = null
        var firstTwo: Int? = null
        val len = nums.size
        nums.forEachIndexed { index, value ->
            if (value == 0) {
                lastZero = swapZero(nums, index, lastZero)
            } else if (value == 2) {
                var firstNonTwoBackwards: Int? = null
                var i = firstTwo?.let { firstTwo!! - 1 } ?: (len - 1)
                while (firstNonTwoBackwards == null && i > index) {
                    if (nums[i] < 2) {
                        firstNonTwoBackwards = i
                    } else {
                        i--
                    }
                }
                if (firstNonTwoBackwards == null) {
                    return
                }
                firstTwo = firstNonTwoBackwards
                nums[index] = nums[firstTwo!!]
                // if it is a 0, move it further up, because we will not revisit this (index is only going up once)
                if (nums[index] == 0) {
                    lastZero = swapZero(nums, index, lastZero)
                }
                nums[firstTwo!!] = 2
            }
            // else are 1s, and they will fall in place as long as we take care of the 0s and 2s
        }
    }

    private fun swapZero(nums: IntArray, index: Int, lastZeroVal: Int?): Int {
        var lastZero = lastZeroVal
        // nums[index] is expected to be 0
        var firstNonZeroForwards: Int? = null
        var i = lastZero?.let { lastZero!! + 1 } ?: 0
        while (firstNonZeroForwards == null && i < index) {
            if (nums[i] > 0) {
                firstNonZeroForwards = i
            } else {
                i++
            }
        }
        if (firstNonZeroForwards == null) {
            lastZero = index
        } else {
            lastZero = firstNonZeroForwards
            nums[index] = nums[lastZero!!]
            nums[lastZero!!] = 0
        }
        return lastZero
    }

    /**
     * Given a "board" of Xs and Os, flip all "trapped" Os to Xs.
     * An O is trapped if it has no connecting Os that can get it to an edge
     */
    fun solve(board: Array<CharArray>): Unit {
        val m = board.size
        if (m == 0) return
        val n = board[0].size
        if (n == 0) return
        val oh = 'O'
        val ex = 'X'
        val justX = CharArray(n)
        (0 until n).forEach { justX[it] = ex }
        val justXStr = String(justX)
        val tracker = Array(m) { _ -> justXStr.toCharArray() }
        // the top and bottom edges, including the corners
        listOf(0, m - 1).forEach { row ->
            board[row].forEachIndexed { col, char ->
                if (char == oh) {
                    tracker[row][col] = oh
                    findNeighbor(board, m, n, row, col, tracker)
                }
            }
        }
        // the two sides, excluding the corners
        if (m > 2) {
            (1..m - 2).forEach { row ->
                listOf(0, n - 1).forEach { col ->
                    if (board[row][col] == oh) {
                        tracker[row][col] = oh
                        findNeighbor(board, m, n, row, col, tracker)
                    }
                }
            }
        }
        // at this point tracker has all the safe Os marked, so just flip everything else to X
        (0 until m).forEach { row ->
            (0 until n).forEach { col ->
                if (tracker[row][col] != oh) {
                    board[row][col] = ex
                }
            }
        }
    }

    // find neighbors that are Os, and mark them as safe using the provided tracker, if they are not already marked
    private fun findNeighbor(board: Array<CharArray>, m: Int, n: Int, row: Int, col: Int, tracker: Array<CharArray>) {
        val oh = 'O'
        fun markSave(row: Int, col: Int) {
            if (board[row][col] == oh && tracker[row][col] != oh) {
                tracker[row][col] = oh
                findNeighbor(board, m, n, row, col, tracker)
            }
        }
        // left
        if (col > 0) {
            markSave(row, col - 1)
        }
        // right
        if (col < n - 1) {
            markSave(row, col + 1)
        }
        // above
        if (row > 0) {
            markSave(row - 1, col)
        }
        // below
        if (row < m - 1) {
            markSave(row + 1, col)
        }
    }

    /**
     * A basic calculator that takes only non-negative integers and + - * /
     * No parenthesis
     * This solution is far from optimal, the best answers all walk the string one character at a time
     * and just multiply by 10 if a number is encountered, and if +/- is encountered, it does accordingly,
     * but if * / is encountered, then it modifies the running argument, until we get to the next +/- or
     * if we get to the end
     */
    fun calculate(s: String): Int {
        // The Int in 'second' is the sign, 1 for plus and -1 for minus
        val plusOrMinus = mutableListOf<Pair<String, Int>>()
        var next = 0
        var rest = s
        var sign = 0
        do {
            val plus = rest.indexOf('+')
            val minus = rest.indexOf('-')
            if (plus > 0 && (minus < 0 || plus < minus)) {
                next = plus
                sign = 1
            } else if (minus > 0 && (plus < 0 || minus < plus)) {
                next = minus
                sign = -1
            } else {
                sign = 0
            }
            if (sign != 0) {
                plusOrMinus.add(Pair(rest.substring(0, next), sign))
                rest = rest.substring(next + 1)
            }
        } while (sign != 0)
        // the last chunk, plus or minus don't matter
        plusOrMinus.add(Pair(rest, 1))
        var answer = 0
        fun handleMultiplyDivide(s: String): Int {
            try {
                val i = s.trim().toInt()
                return i
            } catch (e: Exception) {
                // fine, moving on
            }
            val multiply = s.lastIndexOf('*')
            val divide = s.lastIndexOf('/')
            when {
                (multiply <= 0 || multiply < divide) -> {
                    val sub = handleMultiplyDivide(s.substring(0, divide)) /
                            handleMultiplyDivide(s.substring(divide + 1))
                    // println("$s = $sub")
                    return sub
                }
                (divide <= 0 || multiply > divide) -> {
                    val sub = handleMultiplyDivide(s.substring(0, multiply)) *
                            handleMultiplyDivide(s.substring(multiply + 1))
                    // println("$s = $sub")
                    return sub
                }
            }
            throw IllegalArgumentException(s)
        }

        var lastPlusOrMinus = 1
        plusOrMinus.forEach {
            answer = answer + lastPlusOrMinus * handleMultiplyDivide(it.first)
            lastPlusOrMinus = it.second
        }
        return answer
    }

    /**
     * Same as above, but this one is more recursive and failed the submission because of memory
     */
    fun calculate2(s: String): Int {
        try {
            val i = s.trim().toInt()
            return i
        } catch (e: Exception) {
            // fine, moving on
        }
        s.indexOf('+').let {
            if (it > 0) {
                val sub = calculate(s.substring(0, it)) + calculate(s.substring(it + 1))
                println("$s = $sub")
                return sub
            }
        }
        s.lastIndexOf('-').let {
            if (it > 0) {
                val sub = calculate(s.substring(0, it)) - calculate(s.substring(it + 1))
                println("$s = $sub")
                return sub
            }
        }
        val multiply = s.lastIndexOf('*')
        val divide = s.lastIndexOf('/')
        when {
            (multiply <= 0 || multiply < divide) -> {
                val sub = calculate(s.substring(0, divide)) / calculate(s.substring(divide + 1))
                println("$s = $sub")
                return sub
            }
            (divide <= 0 || multiply > divide) -> {
                val sub = calculate(s.substring(0, multiply)) * calculate(s.substring(multiply + 1))
                println("$s = $sub")
                return sub
            }
        }
        throw IllegalArgumentException(s)
    }

    /**
     * Change a given linked list to odd even: {1,2,3,4,5} -> {1,3,5,2,4}
     */
    fun oddEvenList(head: ListNode?): ListNode? {
        if (head?.next == null) return head
        val second = head.next
        var curOdd = head
        var curEven = second
        while (curOdd != null && curEven != null) {
            // thisOdd->thisEven->nextOdd->nextEven
            val thisOdd = curOdd!!
            val thisEven = curEven!!
            val nextOdd = thisEven.next
            val nextEven = nextOdd?.next
            // println("=== ${thisOdd.`val`}, ${thisEven.`val`}")
            if (nextOdd != null) {
                thisOdd.next = nextOdd
                if (nextEven != null) {
                    thisEven.next = nextEven
                    curOdd = nextOdd
                    curEven = nextEven
                    // println("${nextOdd.`val`}, ${nextEven.`val`}")
                } else {
                    // we stopped at nextOdd
                    nextOdd.next = second
                    curEven.next = null
                    curEven = null
                }
            } else {
                // we stopped at thisEven
                thisOdd.next = second
                curEven.next = null
                curOdd = null
            }
        }
        return head
    }

    /**
     * Given an array of integers of length n + 1 consisting of numbers in [1 .. n], where each number in the array
     * appears at most once, except one, which may appear two or more times. Find that one repeating number.
     * This works, but the best solution from LeetCode was to modify the array (temporarily) and set a number to -1
     * times it if a number of that index is seen, so if it is seen a second time then we know.
     * For example [5, 3, 2, 2, 1] would have set 3 to -3 on the first 2 and then the second 2 will see the -3 and know.
     * Before returning we can switch all negative numbers back to positive.
     */
    fun findDuplicate(nums: IntArray): Int {
        /*
         three solutions, each one will violate at least one constraint stated in the problem description
         1. sort, then walk, this will either require extra memory space or modify the array
         2. create a map of seen numbers and counts, this will require extra memory space
         3. nested loops, checks nums[i] == nums[j] where i != j, this will require O(n^2)
         */
        /* simple solution, but wrong, because it assumes all numbers appear
        val len = nums.size - 1
        return nums.sum() - (1 + len) * len / 2
         */
        val sorted = nums.sortedArray()
        var lastNum = 0
        sorted.forEach {
            if (it == lastNum) {
                return it
            }
            lastNum = it
        }
        return 0
    }

    /**
     * Given a chess board of n x n size, find all boards that can fit n queens on it
     * without having any pair of queens that can "take" each other
     */
    fun solveNQueens(n: Int): List<List<String>> {
        val answers = mutableListOf<List<Int>>()
        (0 until n).forEach {
            addNumber(mutableListOf(), it, n, answers)
        }
        val stringAnswers = answers.map {
            it.map { rowQ ->
                String((0 until n).map { index ->
                    when {
                        index == rowQ -> 'Q'
                        else -> '.'
                    }
                }.toCharArray())
            }
        }
        stringAnswers.forEach {
            it.forEach { row -> println(row) }
            println("")
        }
        println("${stringAnswers.size} number of boards")
        return stringAnswers
    }

    /**
     * A board is abstracted to a list of n integers from 0 to n-1, the 0th index is the first row, and so on.
     * The value at a particular index is the column where the queen is on that row. Obviously each row will
     * have one and only one queen on it, so we build up the board one row at a time, fanning out to cover all
     * possibilities. The big-O should be n^n, so more than n!, we could make it n! if we skip all the indices
     * that already has a queen on it before we fan out, but that itself takes time too, so not sure if it is
     * actually faster.
     */
    private fun addNumber(candidate: List<Int>, num: Int, n: Int, answers: MutableList<List<Int>>) {
        // check if this number can be added up to this point
        // (i.e. whether a new row with a queen at index num can be added)
        val len = candidate.size
        candidate.forEachIndexed { index, atIndex ->
            // check the vertical
            if (num == atIndex) return
            // check the +1 diagonal (NE-SW)
            if (num + (len - index) == atIndex) return
            // check the -1 diagonal (NW-SE)
            if (num - (len - index) == atIndex) return
        }
        // the horizontal is given, because we are about to add it and nothing was there before we do,
        // so at this point num is good, so we add it and then go one level deeper
        val newCandidate = candidate.toMutableList()
        newCandidate.add(num)
        // the board is filled and so we have a winner
        if (newCandidate.size == n) {
            answers.add(newCandidate)
        } else {
            // otherwise, add the next row
            // Here, instead of 0 until n we could skip all the numbers that are already in newCandidate, like this,
            // ((0 until n).toSet() subtract newCandidate.toSet()).forEach {
            // but LeetCode, on making this change, resulted in a slower run (but that is not reliable, I know)
            (0 until n).forEach {
                addNumber(newCandidate, it, n, answers)
            }
        }
    }

    fun compareVersion(version1: String, version2: String): Int {
        val len1 = version1.length
        val len2 = version2.length
        var next1: Int = 0
        var next2: Int = 0
        var dot1: Int = -1
        var dot2: Int = -1
        do {
            next1 = if (dot1 != len1) {
                val newDot1 = version1.indexOf('.', dot1 + 1)
                val temp = dot1
                val ver = if (newDot1 > 0) {
                    dot1 = newDot1
                    version1.substring(temp + 1, newDot1).toInt()
                } else {
                    dot1 = len1
                    version1.substring(temp + 1).toInt()
                }
                ver
            } else {
                0
            }
            next2 = if (dot2 != len2) {
                val newDot2 = version2.indexOf('.', dot2 + 1)
                val temp = dot2
                val ver = if (newDot2 > 0) {
                    dot2 = newDot2
                    version2.substring(temp + 1, newDot2).toInt()
                } else {
                    dot2 = len2
                    version2.substring(temp + 1).toInt()
                }
                ver
            } else {
                0
            }
            // println("$next1, $next2")
            if (next1 > next2) {
                return 1
            } else if (next1 < next2) {
                return -1
            } // else the version at this stage is the same
        } while (dot1 != len1 || dot2 != len2)
        return 0
    }

    fun numIslands(grid: Array<CharArray>): Int {
        val marked = mutableMapOf<Pair<Int, Int>, Int>()
        val width = grid[0].size
        val height = grid.size
        val numIslands = AtomicInteger(0)
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, entry ->
                if (entry == '1') {
                    markNeighbor(x, y, width, height, grid, null, numIslands, marked)
                }
            }
        }
        return numIslands.get()
    }

    private fun markNeighbor(
        x: Int, y: Int, width: Int, height: Int, grid: Array<CharArray>,
        num: Int?, numIslands: AtomicInteger, marked: MutableMap<Pair<Int, Int>, Int>
    ) {
        val value = grid[y][x]
        // println("val $value")
        if (value == '0') {
            return
        }
        val xY = Pair(x, y)
        if (marked[xY] == null) {
            val islandNum = num ?: numIslands.incrementAndGet()
            // println("-- $islandNum")
            marked[xY] = islandNum
            // mark our neighbors
            if (x > 0) {
                // left
                markNeighbor(x - 1, y, width, height, grid, islandNum, numIslands, marked)
            }
            if (x < width - 1) {
                // right
                markNeighbor(x + 1, y, width, height, grid, islandNum, numIslands, marked)
            }
            if (y > 0) {
                // above
                markNeighbor(x, y - 1, width, height, grid, islandNum, numIslands, marked)
            }
            if (y < height - 1) {
                // below
                markNeighbor(x, y + 1, width, height, grid, islandNum, numIslands, marked)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = Solution()
            println(
                sol.numIslands(
                    arrayOf(
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('1', '1', '0', '0', '0'),
                        charArrayOf('0', '0', '1', '0', '0'),
                        charArrayOf('0', '0', '0', '1', '1'),
                    )
                )
            )
            /*
            println(sol.compareVersion("1.01.0.00", "1.1"))
            val counter = AtomicInteger(0)
            sol.solveNQueens(8)
            val five = ListNode(5)
            val four = ListNode(4, five)
            val three = ListNode(3, four)
            val two = ListNode(2, three)
            val one = ListNode(1, two)
            println(sol.findDuplicate(intArrayOf(1, 2, 2, 2, 3)))
            println(sol.oddEvenList(one))
            println(sol.calculate("2 + 3 - 5 + 6 * 8 / 9 - 2"))
            println(sol.calculate("1-1-1"))
            val board = listOf(
                "XXOX",
                "OOXX",
                "XXOX",
                "XXXO"
            ).map { it.toCharArray() }.toTypedArray()
            sol.solve(board)
            board.toList().forEach { println(String(it)) }
            val arr = intArrayOf(0, 2, 1, 2, 0, 1, 2, 2, 2, 1, 2, 0)
            sol.sortColors(arr)
            println(arr.toList())
            println("${sol.myPowByDivide(2.0, 25, counter)}, ${counter.get()}")
            println(sol.removeNthFromEnd(one, 2))
            println("answer: ${sol.isValidSudoku(
                listOf(
                    charArrayOf('5', '3', '.', '.', '7', '.', '.', '.', '.'),
                    charArrayOf('6', '.', '.', '1', '9', '5', '.', '.', '.'),
                    charArrayOf('.', '9', '8', '.', '.', '.', '.', '6', '.'),
                    charArrayOf('8', '.', '.', '.', '6', '.', '.', '.', '3'),
                    charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
                    charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
                    charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
                    charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
                    charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9'),
                ).toTypedArray()
            )}")
             */
        }
    }
}