package com.example.tubesppb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDAO {
    @Query("SELECT * FROM search_history LIMIT 1")
    fun getHistoryData(): Flow<List<History>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    @Query("DELETE FROM search_history")
    fun delHistory()
}