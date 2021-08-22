package com.example.coindemo.repository

import com.example.coindemo.data.remote.CoinService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoinDetailsRepository @Inject constructor(
    private val coinService: CoinService,
    private val ioDispatcher: CoroutineDispatcher
) : BaseRepository() {

    suspend fun getMarketChart(id: String, days: Int, interval: String) =
        flow {
            val response = getResponse(request = {
                coinService.getMarketChart(
                    id = id,
                    days = days,
                    interval = interval
                )
            })
            emit(response)
        }.flowOn(ioDispatcher)
}