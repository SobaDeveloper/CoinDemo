package com.example.coindemo.model


import com.google.gson.annotations.SerializedName

data class GetMarketChartResponse(
    @SerializedName("market_caps") val marketCaps: List<List<Float>>,
    @SerializedName("prices") val prices: List<List<Float>>,
    @SerializedName("total_volumes") val totalVolumes: List<List<Float>>
)