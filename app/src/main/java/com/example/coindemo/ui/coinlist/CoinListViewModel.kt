package com.example.coindemo.ui.coinlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coindemo.model.Coin
import com.example.coindemo.repository.CoinListRepository
import com.example.coindemo.ui.common.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(private val repository: CoinListRepository) :
    ViewModel() {

    private val _viewStateLiveData = MutableLiveData<ViewState<List<Coin>>>()
    val viewStateLiveData: LiveData<ViewState<List<Coin>>> = _viewStateLiveData

    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            repository.getCoins()
                .onStart { _viewStateLiveData.value = ViewState.Loading }
                .map { ViewState.setState(it) }
                .collect {
                    _viewStateLiveData.value = it
                }
        }
    }
}