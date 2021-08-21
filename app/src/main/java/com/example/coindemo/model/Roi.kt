package com.example.coindemo.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Roi(
    @SerializedName("currency") val currency: String,
    @SerializedName("percentage") val percentage: Double,
    @SerializedName("times") val times: Double
) : Parcelable