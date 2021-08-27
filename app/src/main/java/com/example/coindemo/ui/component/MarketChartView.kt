package com.example.coindemo.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.example.coindemo.R
import com.example.coindemo.utils.FormatterExtensions.toMonthDay
import com.example.coindemo.utils.ViewExtensions.color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight

class MarketChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LineChart(context, attrs) {

    var listener: Listener? = null
    lateinit var dataSet: LineDataSet

    init {
        setup()
        marker = object : MarkerView(context, R.layout.marker_view) {
            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                listener?.setChartPrice(e?.x?.toLong()!!, e.y.toDouble())
                super.refreshContent(e, highlight)
            }
        }
    }

    private fun LineChart.setup() = this.apply {
        setDrawBorders(false)
        setDrawGridBackground(false)
        setScaleEnabled(false)
        setTouchEnabled(true)
        description.isEnabled = false
        legend.isEnabled = false
        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
        }
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        extraBottomOffset = 20f
        animateXY(200, 200, Easing.EaseInOutCubic)
        setNoDataText(context.getString(R.string.loading_data))
    }

    fun plotChart(prices: List<List<Float>>, coinColor: Int?) {
        val lineData = getDataSet(prices, coinColor)
        data = lineData
        invalidate()
    }

    private fun getDataSet(prices: List<List<Float>>, coinColor: Int?): LineData {
        val entries = mutableListOf<Entry>()
        prices.forEach { entries.add(Entry(it[0], it[1])) }
        dataSet = LineDataSet(entries, "").also {
            it.setup(context, coinColor)
        }
        return LineData(dataSet)
    }

    private fun showXAxis() = xAxis.apply {
        setDrawLabels(true)
        textSize = 13f
        setLabelCount(5, true)
        position = XAxis.XAxisPosition.BOTTOM
        textColor = context.color(R.color.white)
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toLong().toMonthDay()
            }
        }
    }

    private fun hideXAxis() = xAxis.setDrawLabels(false)

    private fun LineDataSet.setup(context: Context, coinColor: Int?) =
        this@setup.apply {
            setDrawCircles(false)
            setDrawFilled(false)
            setDrawValues(false)
            enableDashedHighlightLine(12f, 6f, 0f)
            setDrawHorizontalHighlightIndicator(false)
            highLightColor = context.color(R.color.red)
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            color = coinColor ?: context.color(R.color.white)
        }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener(scrollView: CustomScrollView) {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    listener?.onChartActionUp()
                    listener?.setCurrentPrice()
                    scrollView.enableScrolling = true
                    highlightValue(null)
                    hideXAxis()
                }
                MotionEvent.ACTION_DOWN -> {
                    listener?.onChartActionDown()
                    scrollView.enableScrolling = false
                }
            }
            false
        }
    }

    interface Listener {
        fun setCurrentPrice()
        fun setChartPrice(time: Long, price: Double)
        fun onChartActionDown()
        fun onChartActionUp()
    }
}