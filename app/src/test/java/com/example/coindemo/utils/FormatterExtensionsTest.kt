package com.example.coindemo.utils

import com.example.coindemo.utils.FormatterExtensions.roundTwoPlaces
import com.example.coindemo.utils.FormatterExtensions.toLocalDate
import com.example.coindemo.utils.FormatterExtensions.toLocalDateTime
import com.example.coindemo.utils.FormatterExtensions.toMonthDay
import com.example.coindemo.utils.FormatterExtensions.withSuffix
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatterExtensionsTest {

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
        val expected = "1/14"
        assertEquals(expected, input.toMonthDay())
    }

    @Test
    fun testToLocalDate() {
        val input = 1234567890L
        val expected = "Jan 14, 1970"
        assertEquals(expected, input.toLocalDate())
    }

    @Test
    fun testToLocalDateTime() {
        val input = 1234567890L
        val expected = "Jan 14, 10:56 PM"
        assertEquals(expected, input.toLocalDateTime())
    }
}