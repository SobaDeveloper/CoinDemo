package com.example.coindemo.data.local

import androidx.room.*
import com.example.coindemo.model.Coin

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin ORDER BY marketCapRank ASC")
    fun getAll(): List<Coin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coins: List<Coin>)

    @Delete
    fun delete(coin: Coin)

    @Query("DELETE FROM coin")
    fun deleteAll()
}