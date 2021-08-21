package com.example.coindemo

import com.example.coindemo.utils.FormattingUtil.formatCurrency
import org.junit.Assert.*

import org.junit.Test

class FormattingUtilTest {

    @Test
    fun formatCurrency() {
        val double = 1234.563
        assertEquals(double.formatCurrency(), "$1,234.56")
    }
}