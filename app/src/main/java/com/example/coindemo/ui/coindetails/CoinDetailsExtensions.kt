package com.example.coindemo.ui.coindetails

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.coindemo.R
import com.example.coindemo.utils.FormatterExtensions.formatCurrency
import com.example.coindemo.utils.FormatterExtensions.roundTwoPlaces
import com.example.coindemo.utils.FormatterExtensions.toMonthDay
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import kotlin.math.abs

object CoinDetailsExtensions {

    fun LinearLayout.setDateLabels(prices: List<List<Float>>) {
        val labels = arrayListOf<Float>()
        prices.forEach { labels.add(it[0]) }
        val size = labels.size
        removeAllViews()
        (0..4).forEach { i ->
            var index = (i * 0.25 * size).toInt()
            if (index == size) index = size - 1
            val value = labels[index].toLong().toMonthDay()
            addView(getLabel(context, value))
        }
    }

    private fun getLabel(context: Context, value: String): TextView =
        TextView(context).also {
            it.text = value
            it.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.CENTER_VERTICAL
            }
            it.layoutParams = params
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