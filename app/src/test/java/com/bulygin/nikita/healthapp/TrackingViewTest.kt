package com.bulygin.nikita.healthapp

import org.junit.Assert.assertEquals
import org.junit.Test


class TrackingViewTest {

    @Test
    fun onTouch() {
        assertEquals(0, this.atoi(""))
        assertEquals(1234, this.atoi("  1234"))
        assertEquals(-12, this.atoi("-12"))
        assertEquals(-12, this.atoi("  -12"))
        assertEquals(-12, this.atoi("  -12 "))
        assertEquals(12, this.atoi("  12 "))
    }

    private fun atoi(s: String): Int {
        val size = s.length
        if (size == 0) {
            return 0
        }
        for (i in 0..size) {
            val ch = s[i]
            if (isBeginOfNumber(ch)) {
                return (getNumberFromIndex(i, s))
            }
        }
        return 0
    }

    private fun getNumberFromIndex(i: Int, s: String): Int {
        val ch = s[i].toInt()
        val size = s.length - 1
        var value = if (ch == 45) -1 else 1
        var curInt: Int
        for (index in i + 1..size) {
            curInt = s[index].toInt() - 48
            if (curInt < 0 || curInt > 9) {
                return value
            }
            value = value * 10 + curInt
        }
        return value
    }

    private fun isBeginOfNumber(ch: Char): Boolean {
        val i = ch.toInt()
        return i == 43 || i == 45 || isDigit(i)
    }

    private fun isDigit(i: Int): Boolean {
        return i > 47 && i < 58
    }

}