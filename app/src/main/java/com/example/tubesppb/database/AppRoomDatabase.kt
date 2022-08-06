package com.example.tubesppb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Book::class, History::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDAO
    abstract fun historyDao(): HistoryDAO

    private class BookDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateBookDatabase(database.bookDao())
                    populateHistoryDatabase(database.historyDao())
                }
            }
        }

        suspend fun populateBookDatabase(bookDao: BookDAO) {
            bookDao.deleteAll()

            //to input book data into database
            /*var book = Book("Call of Cthulhu", "H.P. Lovecraft", "Genre Cosmic Horror")
            bookDao.insert(book)
            book = Book("All Tomorrow", "C.M. Kösemen", "Genre Sci-fi")
            bookDao.insert(book)
            book = Book("Grimgar of Fantasy and Ash", "Ao Jyumonji", "Genre Fantasy")
            bookDao.insert(book)*/
        }

        suspend fun populateHistoryDatabase(historyDao: HistoryDAO) {
            historyDao.delHistory()

            val history = History("fiction")
            historyDao.insert(history)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getBookDatabase(context: Context, scope: CoroutineScope): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "book_database"
                ).addCallback(BookDatabaseCallback(scope)).build()
                INSTANCE = instance

                instance
            }
        }

        fun getHistoryDatabase(context: Context, scope: CoroutineScope): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "search_history"
                ).addCallback(BookDatabaseCallback(scope)).build()
                INSTANCE = instance

                instance
            }
        }
    }
}