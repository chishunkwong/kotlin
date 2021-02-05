package com.ckwong

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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val five = ListNode(5)
            val four = ListNode(4, five)
            val three = ListNode(3, four)
            val two = ListNode(2, three)
            val one = ListNode(1, two)
            println(Solution().removeNthFromEnd(one, 2))
            /*
            println("answer: ${Solution().isValidSudoku(
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