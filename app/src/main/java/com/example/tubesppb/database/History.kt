package com.example.tubesppb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
class History(
    @ColumnInfo(name = "category") val category: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)