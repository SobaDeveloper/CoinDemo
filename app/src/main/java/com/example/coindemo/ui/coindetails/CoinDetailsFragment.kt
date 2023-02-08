package com.example.coindemo.ui.coindetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coindemo.R
import com.example.coindemo.databinding.FragmentCoinDetailsBinding
import com.example.coindemo.ui.coindetails.CoinDetailsExtensions.setDateLabels
import com.example.coindemo.ui.coindetails.CoinDetailsExtensions.setPriceAndPercentChange
import com.example.coindemo.ui.common.ViewState
import com.example.coindemo.ui.component.MarketChartView
import com.example.coindemo.utils.AnimationUtil.crossFadeTo
import com.example.coindemo.utils.FormatterExtensions.formatCurrency
import com.example.coindemo.utils.FormatterExtensions.toLocalDate
import com.example.coindemo.utils.FormatterExtensions.toLocalDateTime
import com.example.coindemo.utils.PaletteUtil
import com.example.coindemo.utils.ViewExtensions.color
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.invisible
import com.example.coindemo.utils.ViewExtensions.showErrorSnackBar
import com.example.coindemo.utils.ViewExtensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        args.coin.apply {
            setCoinColor(this.imageUrl)
            binding.toolBar.title = this.name
            binding.marketStatsView.bind(this)
            viewModel.getMarketPrices(this.id)
        }
        setCurrentPrice()

        binding.marketChartView.apply {
            setOnTouchListener(binding.scrollView)
            listener = this@CoinDetailsFragment
        }
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
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    binding.marketChartView.plotChart(it.data.prices, viewModel.coinColor)
                    binding.labelContainer.setDateLabels(it.data.prices)
                }
                is ViewState.Error -> it.message.showErrorSnackBar(requireView())
            }
        }
    }

    override fun setCurrentPrice() {
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
            if (viewModel.isIntervalHourly()) time.toLocalDateTime() else time.toLocalDate()
        binding.tvPrice.text = price.formatCurrency()
        binding.tvPriceChange.invisible()
    }

    override fun onChartActionDown() = binding.chipGroup.crossFadeTo(binding.labelContainer)

    override fun onChartActionUp() = binding.labelContainer.crossFadeTo(binding.chipGroup)

    private fun setCoinColor(imageUrl: String) {
        lifecycleScope.launch {
            val color = PaletteUtil.getPaletteColor(requireContext(), imageUrl)
                ?: requireContext().color(R.color.white)
            viewModel.coinColor = color
        }
    }
}