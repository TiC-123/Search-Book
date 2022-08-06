package com.example.tubesppb

import android.app.Application
import com.example.tubesppb.repository.BookRepository
import com.example.tubesppb.database.AppRoomDatabase
import com.example.tubesppb.repository.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val bookDatabase by lazy { AppRoomDatabase.getBookDatabase(this, applicationScope) }
    private val historyDatabase by lazy { AppRoomDatabase.getHistoryDatabase(this, applicationScope) }
    val bookRepository by lazy { BookRepository(bookDatabase.bookDao()) }
    val historyRepository by lazy { HistoryRepository(historyDatabase.historyDao()) }
}