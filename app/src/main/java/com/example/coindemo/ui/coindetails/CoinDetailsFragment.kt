package com.example.coindemo.ui.coindetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coindemo.R
import com.example.coindemo.databinding.FragmentCoinDetailsBinding
import com.example.coindemo.ui.common.ViewState
import com.example.coindemo.utils.FormattingUtil.formatCurrency
import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.FormattingUtil.toLocalDate
import com.example.coindemo.utils.FormattingUtil.toLocalDateTime
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.invisible
import com.example.coindemo.utils.ViewExtensions.showErrorSnackBar
import com.example.coindemo.utils.ViewExtensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class CoinDetailsFragment : Fragment(), MarketChartView.Listener {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        val coin = args.coin
        setDefaultPrice()
        binding.toolBar.title = coin.name
        binding.marketChartView.apply {
            setOnTouchListener(binding.scrollView)
            listener = this@CoinDetailsFragment
        }
        binding.marketStatsView.bind(coin)
        viewModel.getMarketPrices(coin.id)
    }

    private fun setListeners() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.getMarketPrices(coinId = args.coin.id, chipId = checkedId)
        }
    }

    private fun setLiveData() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> binding.marketChartView.plotChart(it.data.prices)
                is ViewState.Error -> it.message.showErrorSnackBar(requireView())
            }
        })
    }

    override fun setDefaultPrice() {
        val coin = args.coin
        binding.tvDate.text = getString(R.string.s_price, coin.name)
        binding.tvPrice.text = coin.currentPrice.formatCurrency()
        binding.tvPriceChange.apply {
            visible()
            compoundDrawablePadding = 6.dp
            setPriceAndPercentChange(coin.priceChange24h, coin.priceChangePercentage24h)
        }
    }

    override fun setChartPrice(time: Long, price: Double) {
        binding.tvDate.text =
            if (viewModel.isHourlyInterval()) time.toLocalDateTime() else time.toLocalDate()
        binding.tvPrice.text = price.formatCurrency()
        binding.tvPriceChange.invisible()
    }

    private fun TextView.setPriceAndPercentChange(priceChange: Double, percentChange: Double) {
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