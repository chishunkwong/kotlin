package com.ckwong

class CitiSolution {

    fun foo(): String {
        return "bar"
    }
    
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sol = CitiSolution()
            print(sol.foo())
        }
    }
}