package com.example.coindemo.utils

import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

object FormattingUtil {

    fun Double.formatCurrency(currencyCode: String = "USD", allowDecimals: Boolean = true): String =
        NumberFormat.getCurrencyInstance()
            .also {
                it.maximumFractionDigits = if (allowDecimals) if (this > 1) 2 else 6 else 0
                it.currency = Currency.getInstance(currencyCode)
            }.format(this)

    fun Float.formatCurrency(currencyCode: String = "USD", allowDecimals: Boolean = true): String =
        NumberFormat.getCurrencyInstance()
            .also {
                it.maximumFractionDigits = if (allowDecimals) if (this > 1) 2 else 6 else 0
                it.currency = Currency.getInstance(currencyCode)
            }.format(this)

    fun Double.roundTwoPlaces(): String = String.format("%.2f", this)

    fun Double.withSuffix(): String {
        if (this < 1000) return "" + this
        val exp = (ln(this) / ln(1000.0)).toInt()
        return String.format(
            "%.3f %c", this / 1000.00.pow(exp.toDouble()), "KMBTPE"[exp - 1]
        )
    }

    fun Long.toMonthDay(): String =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate().toString()
}