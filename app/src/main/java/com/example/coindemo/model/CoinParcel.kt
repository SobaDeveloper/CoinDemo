package com.example.coindemo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinParcel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val marketCap: Double,
    val totalVolume: Float,
    val circulatingSupply: Double,
    val marketCapChangePercentage24h: Double,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
) : Parcelable