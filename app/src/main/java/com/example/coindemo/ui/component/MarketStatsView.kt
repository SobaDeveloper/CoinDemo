package com.example.coindemo.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.coindemo.databinding.ViewMarketStatsBinding
import com.example.coindemo.model.CoinParcel
import com.example.coindemo.utils.FormatterExtensions.formatCurrency
import com.example.coindemo.utils.FormatterExtensions.roundTwoPlaces
import com.example.coindemo.utils.FormatterExtensions.withSuffix
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.setColor

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

    fun bind(coin: CoinParcel) {
        setMarketCap(coin.marketCap)
        setVolume(coin.totalVolume)
        setCirculatingSupply(coin.circulatingSupply)
        setPercentChange24h(coin.marketCapChangePercentage24h)
    }

    private fun setMarketCap(marketCap: Double) {
        binding.marketCapValue.text = marketCap.formatCurrency(allowDecimals = false)
    }

    private fun setVolume(totalVolume: Float) {
        binding.volumeValue.text = totalVolume.formatCurrency(allowDecimals = false)
    }

    private fun setCirculatingSupply(supply: Double) {
        binding.supplyValue.text = supply.withSuffix()
    }

    private fun setPercentChange24h(percent: Double) {
        binding.percentChangeValue.apply {
            text = percent.roundTwoPlaces().appendPercentage()
            compoundDrawablePadding = 6.dp
            setColor(percent >= 0)
        }
    }
}