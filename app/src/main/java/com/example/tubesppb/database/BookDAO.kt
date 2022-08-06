package com.example.tubesppb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDAO {
    @Query("SELECT * FROM book_table ORDER BY title ASC")
    fun getFavoriteBook(): Flow<List<Book>>

    @Query("SELECT * FROM book_table LIMIT 3")
    fun getSomeBookmark(): Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE title LIKE :title")
    fun getCertainBook(title: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Query("DELETE FROM book_table")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM book_table WHERE title = :title)")
    fun bookCheck(title: String): Boolean

    @Query("DELETE FROM book_table WHERE title = :title")
    fun delBook(title: String)
}