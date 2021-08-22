package com.example.coindemo.utils

import android.widget.TextView
import com.example.coindemo.R
import com.example.coindemo.utils.FormattingUtil.formatCurrency
import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
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

    fun Long.toLocalDate(): String {
        val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return formatter.format(date)
    }

    fun Long.toLocalDateTime(): String {
        val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("E, MMM dd, h:mm a")
        return formatter.format(date)
    }

    fun TextView.setPriceAndPercentChange(priceChange: Double, percentChange: Double) {
        val sb = StringBuilder()
        if (priceChange >= 0) {
            sb.append("+ ")
            setTextColor(context.getColor(R.color.green))
        } else {
            sb.append("- ")
            setTextColor(context.getColor(R.color.red))
        }
        sb.append(abs(priceChange).formatCurrency())
        sb.append(" (${abs(percentChange).roundTwoPlaces().appendPercentage()})")
        text = sb.toString()
    }
}