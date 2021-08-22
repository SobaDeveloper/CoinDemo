package com.example.coindemo.repository

import com.example.coindemo.data.Result
import com.example.coindemo.data.local.CoinDao
import com.example.coindemo.data.remote.CoinService
import com.example.coindemo.model.Coin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoinListRepository @Inject constructor(
    private val coinService: CoinService,
    private val coinDao: CoinDao,
    private val ioDispatcher: CoroutineDispatcher
) : BaseRepository() {

    suspend fun getCoins() = flow {
        try {
            emit(getCachedData())

            val response = coinService.getCoins()
            val body = response.body()

            if (response.isSuccessful && body != null)
                coinDao.insertAll(body)
            else emit(Result.Error(response.message()))

            emit(getCachedData())
        } catch (e: Throwable) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(ioDispatcher)

    private fun getCachedData(): Result<List<Coin>> = coinDao.getAll().let { Result.Success(it) }
}