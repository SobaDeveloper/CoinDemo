package com.example.coindemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coindemo.model.Coin

@Database(entities = [Coin::class], version = 2)
@TypeConverters(DataConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}