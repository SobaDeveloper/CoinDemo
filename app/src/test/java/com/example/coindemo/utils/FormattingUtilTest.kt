package com.example.coindemo.utils

import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.FormattingUtil.toMonthDay
import com.example.coindemo.utils.FormattingUtil.withSuffix
import org.junit.Assert.assertEquals
import org.junit.Test

class FormattingUtilTest {

    @Test
    fun testRoundTwoPlaces() {
        val input = 123.4506
        val expected = "123.45"
        assertEquals(expected, input.roundTwoPlaces())
    }

    @Test
    fun testWithSuffix1() {
        val input = 1234567.12
        val expected = "1.235 M"
        assertEquals(expected, input.withSuffix())
    }

    @Test
    fun testWithSuffix2() {
        val input = 1234567890.12
        val expected = "1.235 B"
        assertEquals(expected, input.withSuffix())
    }

    @Test
    fun testToMonthDay() {
        val input = 1234567890L
        val expected = "1970-01-14"
        assertEquals(expected, input.toMonthDay())
    }
}