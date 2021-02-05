package com.ckwong

class ListNode(var `val`: Int) {
    var next: ListNode? = null
    constructor(`val`: Int, next: ListNode) : this(`val`) {
        this.next = next
    }
    override fun toString() = "$`val` ${if (next != null) ", $next" else ""}"
}