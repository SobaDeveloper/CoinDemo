package com.example.coindemo.data.remote

import com.example.coindemo.model.Coin
import com.example.coindemo.model.GetMarketChartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinService {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "USD",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") count: Int = 50,
        @Query("page") page: Int = 1
    ): Response<List<Coin>>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String = "USD",
        @Query("days") days: Int = 30,
        @Query("interval") interval: String = "hourly"
    ): Response<GetMarketChartResponse>
}