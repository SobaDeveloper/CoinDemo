package com.example.coindemo.ui.coinlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coindemo.databinding.FragmentCoinListBinding
import com.example.coindemo.model.Coin
import com.example.coindemo.ui.common.ViewState
import com.example.coindemo.utils.ViewExtensions.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : Fragment(), CoinListAdapter.Listener {

    private var _binding: FragmentCoinListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinListViewModel by viewModels()
    private val coinListAdapter = CoinListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setLiveData()
        setListeners()
    }

    private fun initUi() {
        binding.recyclerView.apply {
            adapter = coinListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun setLiveData() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                is ViewState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    coinListAdapter.submitList(it.data)
                }
                is ViewState.Error -> it.message.showErrorSnackBar(requireView())
            }
        })
    }

    private fun setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getCoins() }
    }

    override fun onCoinSelected(coin: Coin) {
        findNavController().navigate(
            CoinListFragmentDirections.actionCoinListFragmentToCoinDetailsFragment(coin = coin.mapToParcel())
        )
    }
}