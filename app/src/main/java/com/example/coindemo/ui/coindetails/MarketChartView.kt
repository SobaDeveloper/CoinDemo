package com.example.coindemo.ui.coindetails

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.example.coindemo.R
import com.example.coindemo.utils.CustomScrollView
import com.example.coindemo.utils.ViewExtensions.color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class MarketChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LineChart(context, attrs) {

    var listener: Listener? = null

    fun plotChart(prices: List<List<Float>>) {
        val lineData = getDataSet(prices)
        clear()
        setup()
        data = lineData
        marker = object : MarkerView(context, R.layout.marker_view) {
            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                listener?.setChartPrice(e?.x?.toLong()!!, e.y.toDouble())
                super.refreshContent(e, highlight)
            }

            override fun getOffset(): MPPointF {
                return MPPointF(
                    -(width.toFloat() + height.toFloat() * 0.2f),
                    -height.toFloat() * 1.2f
                )
            }
        }
    }

    private fun getDataSet(prices: List<List<Float>>): LineData {
        val entries = mutableListOf<Entry>()
        prices.forEach { entries.add(Entry(it[0], it[1])) }

        val dataSet = LineDataSet(entries, "").also {
            it.setup(context)
        }
        return LineData(dataSet)
    }

    private fun LineChart.setup() = this.apply {
        setDrawBorders(false)
        setDrawGridBackground(false)
        setScaleEnabled(false)
        setTouchEnabled(true)
        description.isEnabled = false
        legend.isEnabled = false
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        animateXY(300, 300, Easing.EaseInSine)
    }

    private fun LineDataSet.setup(context: Context) = this.apply {
        setDrawCircles(false)
        setDrawFilled(true)
        setDrawValues(false)
        color = context.color(R.color.white)
        highLightColor = context.color(R.color.red)
        lineWidth = 2f
        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener(scrollView: CustomScrollView) {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    scrollView.enableScrolling = true
                    highlightValue(null)
                    listener?.setDefaultPrice()
                }
                MotionEvent.ACTION_DOWN -> scrollView.enableScrolling = false
            }
            false
        }
    }

    interface Listener {
        fun setDefaultPrice()
        fun setChartPrice(time: Long, price: Double)
    }
}