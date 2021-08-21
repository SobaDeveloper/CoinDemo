package com.example.coindemo.ui.coindetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.coindemo.R
import com.example.coindemo.databinding.ViewMarketStatsBinding
import com.example.coindemo.utils.FormattingUtil.formatCurrency
import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.FormattingUtil.withSuffix
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp

class MarketStatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyle: Int = 0
) : ConstraintLayout(context, attrs, defstyle) {

    private var _binding: ViewMarketStatsBinding? = null
    private val binding get() = _binding!!

    init {
        val inflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = ViewMarketStatsBinding.inflate(inflater, this)
    }

    fun setMarketCap(marketCap: Double) {
        binding.marketCapValue.text = marketCap.formatCurrency(allowDecimals = false)
    }

    fun setVolume(totalVolume: Float) {
        binding.volumeValue.text = totalVolume.formatCurrency(allowDecimals = false)
    }

    fun setCirculatingSupply(supply: Double) {
        binding.supplyValue.text = supply.withSuffix()
    }

    fun setPercentChange24h(percent: Double) {
        binding.percentChangeValue.apply {
            text = percent.roundTwoPlaces().appendPercentage()
            compoundDrawablePadding = 6.dp
            setColor(percent >= 0)
        }
    }

    private fun TextView.setColor(bullish: Boolean) {
        if (bullish) {
            setTextColor(context.getColor(R.color.green))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_green, 0)
        } else {
            setTextColor(context.getColor(R.color.red))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_red, 0)
        }
    }
}