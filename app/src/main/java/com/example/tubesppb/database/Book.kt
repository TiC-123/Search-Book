package com.example.tubesppb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
class Book(
    //search buku misal 'titanic' : dapat buku dari api, data simpen ke sini kalau favorit
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "desc") val description: String,
    @ColumnInfo(name = "imageLink") val image: String,
    @ColumnInfo(name = "buyLink") val buy: String,
    @ColumnInfo(name = "preview") val preview: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)