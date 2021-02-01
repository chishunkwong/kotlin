package com.ckwong

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    val n = args[0].toInt()
    if (n < 1) {
        return
    }
    val matrix = Array(n) { IntArray(n) { -1 } }
    peelFourSides(n - 1, 0, 1, matrix)
    for (i in 0 until n) {
        for (j in matrix[i]) {
            print("$j ")
        }
        println("")
    }
}

fun peelFourSides(sideLength: Int, offset: Int, start: Int, matrix: Array<IntArray>) {
    if (sideLength < 0) {
        return
    }
    if (sideLength == 0) {
        // there are not four sides, just the center (for the case of N being odd)
        matrix[offset][offset] = start
        return
    }
    var nextNum = start
    // top edge
    for (i in 0 until sideLength) {
        matrix[offset][offset + i] = nextNum++
    }
    // right side
    for (i in 0 until sideLength) {
        matrix[offset + i][offset + sideLength] = nextNum++
    }
    // bottom edge
    for (i in 0 until sideLength) {
        matrix[offset + sideLength][offset + sideLength - i] = nextNum++
    }
    // left side
    for (i in 0 until sideLength) {
        matrix[offset + sideLength - i][offset] = nextNum++
    }
    // we have peeled the four sides, now recurse into the inner core of the matrix
    peelFourSides(sideLength - 2, offset + 1, nextNum, matrix)
}
