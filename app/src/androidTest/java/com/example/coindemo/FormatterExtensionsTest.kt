package com.example.coindemo

import com.example.coindemo.utils.FormatterExtensions.formatCurrency
import org.junit.Assert.*

import org.junit.Test

class FormatterExtensionsTest {

    @Test
    fun formatCurrency() {
        val double = 1234.563
        assertEquals(double.formatCurrency(), "$1,234.56")
    }
}