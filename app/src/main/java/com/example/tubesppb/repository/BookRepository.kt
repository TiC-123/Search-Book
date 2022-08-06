package com.example.tubesppb.repository

import androidx.annotation.WorkerThread
import com.example.tubesppb.database.Book
import com.example.tubesppb.database.BookDAO
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDAO) {
    val allBooks: Flow<List<Book>> = bookDao.getFavoriteBook()
    val someBooks: Flow<List<Book>> = bookDao.getSomeBookmark()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun bookCheck(title: String): Boolean {
        return bookDao.bookCheck(title)
    }

    fun getCertainBook(title: String): Flow<List<Book>> {
        return bookDao.getCertainBook(title)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(book: Book) {
        bookDao.insert(book)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delBook(title: String) {
        bookDao.delBook(title)
    }
}