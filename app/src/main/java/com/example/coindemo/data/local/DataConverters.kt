package com.example.coindemo.data.local

import androidx.room.TypeConverter
import com.example.coindemo.model.Roi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {

    /*
     * ROI
     */
    @TypeConverter
    fun jsonToRoi(json: String): Roi? {
        val roiType = object : TypeToken<Roi>() {}.type
        return Gson().fromJson(json, roiType)
    }

    @TypeConverter
    fun roiToJson(roi: Roi?): String = Gson().toJson(roi)
}