package com.example.coindemo.ui.coindetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coindemo.R
import com.example.coindemo.databinding.FragmentCoinDetailsBinding
import com.example.coindemo.model.CoinArgs
import com.example.coindemo.ui.common.ViewState
import com.example.coindemo.utils.FormattingUtil.formatCurrency
import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.FormattingUtil.toMonthDay
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.invisible
import com.example.coindemo.utils.ViewExtensions.setColor
import com.example.coindemo.utils.ViewExtensions.showErrorSnackBar
import com.example.coindemo.utils.ViewExtensions.visible
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailsFragment : Fragment() {

    private var _binding: FragmentCoinDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinDetailsViewModel by viewModels()
    private val args by navArgs<CoinDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()
        setLiveData()
    }

    private fun initUi() {
        val coin = args.coin
        setPrice(coin)
        binding.toolBar.title = coin.name
        binding.marketStatsView.apply {
            setMarketCap(coin.marketCap)
            setVolume(coin.totalVolume)
            setCirculatingSupply(coin.circulatingSupply)
            setPercentChange24h(coin.marketCapChangePercentage24h)
        }

        viewModel.getMarketPrices(coin.id)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.getMarketPrices(coinId = args.coin.id, chipId = checkedId)
        }

        binding.lineChart.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                binding.lineChart.highlightValue(null)
                setPrice(args.coin)
            }
            false
        }
    }

    private fun setLiveData() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> { }
                is ViewState.Success -> plotChart(it.data.prices)
                is ViewState.Error -> it.message.showErrorSnackBar(requireView())
            }
        })
    }

    private fun setPrice(coin: CoinArgs) {
        binding.tvDate.text = getString(R.string.s_price, coin.name)
        binding.tvPrice.text = coin.currentPrice.formatCurrency()
        binding.tvPercentChange.apply {
            visible()
            text = coin.priceChangePercentage24h.roundTwoPlaces().appendPercentage()
            compoundDrawablePadding = 6.dp
            setColor(coin.priceChangePercentage24h >= 0)
        }
    }

    private fun plotChart(prices: List<List<Float>>) {
        val lineData = getDataSet(prices)
        binding.lineChart.apply {
            clear()
            setup()
            data = lineData
            marker = object : MarkerView(requireContext(), R.layout.marker_view) {
                override fun refreshContent(e: Entry?, highlight: Highlight?) {
                    binding.tvDate.text = e?.x?.toLong()!!.toMonthDay()
                    binding.tvPrice.text = e.y.toDouble().formatCurrency()
                    binding.tvPercentChange.invisible()
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
    }

    private fun LineChart.setup() = this.apply {
        setScaleEnabled(false)
        setTouchEnabled(true)
        setDrawGridBackground(false)
        description.isEnabled = false
        legend.isEnabled = false
        xAxis.setDrawGridLines(false)
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        animateXY(300, 300, Easing.EaseInSine)
    }

    private fun getDataSet(prices: List<List<Float>>): LineData {
        val entries = mutableListOf<Entry>()
        prices.forEach { entries.add(Entry(it[0], it[1])) }

        val dataSet = LineDataSet(entries, "")
        dataSet.apply {
            setDrawFilled(true)
            setDrawValues(false)
            setDrawCircles(false)
            lineWidth = 2f
            color = ContextCompat.getColor(requireContext(), R.color.white)
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            highLightColor = ContextCompat.getColor(requireContext(), R.color.red)
        }
        return LineData(dataSet)
    }
}