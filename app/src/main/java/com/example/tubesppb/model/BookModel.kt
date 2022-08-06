package com.example.tubesppb.model

data class BookModel(
    val title: String,
    val author: String,
    val description: String,
    val category: String,
    val image: String,
    val buyLink: String,
    val previewLink: String,
    val inDb: Boolean
)
