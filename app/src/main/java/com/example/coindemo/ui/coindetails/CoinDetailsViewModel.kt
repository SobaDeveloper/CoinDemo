package com.example.coindemo.ui.coindetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coindemo.R
import com.example.coindemo.model.GetMarketChartResponse
import com.example.coindemo.repository.CoinDetailsRepository
import com.example.coindemo.ui.common.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(private val repository: CoinDetailsRepository) :
    ViewModel() {

    private val _viewStateLiveData = MutableLiveData<ViewState<GetMarketChartResponse>>()
    val viewStateLiveData: LiveData<ViewState<GetMarketChartResponse>> = _viewStateLiveData

    private val _labelValuesLiveData = MutableLiveData<List<Float>>()
    val labelValuesLiveData: LiveData<List<Float>> = _labelValuesLiveData

    private var selectedInterval = INTERVAL_HOURLY
    var coinColor: Int? = null

    fun getMarketPrices(coinId: String, chipId: Int? = null) {

        val days = daysMap[chipId] ?: DAYS_7
        val interval = intervalMap[chipId] ?: INTERVAL_HOURLY
        selectedInterval = interval

        viewModelScope.launch {
            repository.getMarketChart(coinId, days, interval)
                .onStart { _viewStateLiveData.value = ViewState.Loading }
                .map { ViewState.setState(it) }
                .collect {
                    _viewStateLiveData.value = it
                }
        }
    }

    fun isIntervalHourly() = selectedInterval == INTERVAL_HOURLY

    private val daysMap = mapOf(
        Pair(R.id.chip7, DAYS_7),
        Pair(R.id.chip30, DAYS_30),
        Pair(R.id.chip90, DAYS_90),
        Pair(R.id.chip180, DAYS_180),
        Pair(R.id.chip360, DAYS_360)
    )

    private val intervalMap = mapOf(
        Pair(R.id.chip7, INTERVAL_HOURLY),
        Pair(R.id.chip30, INTERVAL_HOURLY),
        Pair(R.id.chip90, INTERVAL_DAILY),
        Pair(R.id.chip180, INTERVAL_DAILY),
        Pair(R.id.chip360, INTERVAL_DAILY)
    )

    companion object {
        const val DAYS_7 = 7
        const val DAYS_30 = 30
        const val DAYS_90 = 90
        const val DAYS_180 = 180
        const val DAYS_360 = 360

        const val INTERVAL_DAILY = "daily"
        const val INTERVAL_HOURLY = "hourly"
    }
}