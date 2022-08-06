package com.example.tubesppb.repository

import androidx.annotation.WorkerThread
import com.example.tubesppb.database.History
import com.example.tubesppb.database.HistoryDAO
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDAO) {
    val historyData: Flow<List<History>> = historyDao.getHistoryData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(history: History) {
        historyDao.insert(history)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delHistory() {
        historyDao.delHistory()
    }
}