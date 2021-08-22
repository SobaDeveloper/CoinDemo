package com.example.coindemo.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Coin(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("ath") val ath: Double,
    @SerializedName("ath_change_percentage") val athChangePercentage: Double,
    @SerializedName("ath_date") val athDate: String,
    @SerializedName("atl") val atl: Double,
    @SerializedName("atl_change_percentage") val atlChangePercentage: Double,
    @SerializedName("atl_date") val atlDate: String,
    @SerializedName("circulating_supply") val circulatingSupply: Double,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("fully_diluted_valuation") val fullyDilutedValuation: Long,
    @SerializedName("high_24h") val high24h: Double,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("low_24h") val low24h: Double,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("market_cap_change_24h") val marketCapChange24h: Double,
    @SerializedName("market_cap_change_percentage_24h") val marketCapChangePercentage24h: Double,
    @SerializedName("market_cap_rank") val marketCapRank: Int,
    @SerializedName("max_supply") val maxSupply: Float,
    @SerializedName("name") val name: String,
    @SerializedName("price_change_24h") val priceChange24h: Double,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerializedName("roi") val roi: Roi? = null,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("total_supply") val totalSupply: Float,
    @SerializedName("total_volume") val totalVolume: Float
) : Parcelable {

    fun mapToParcel() = CoinParcel(
        id,
        name,
        currentPrice,
        marketCap,
        totalVolume,
        circulatingSupply,
        marketCapChangePercentage24h,
        priceChange24h,
        priceChangePercentage24h
    )
}