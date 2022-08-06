package com.example.tubesppb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesppb.Application
import com.example.tubesppb.R
import com.example.tubesppb.adapter.BookFavoriteAdapter
import com.example.tubesppb.viewmodel.BookViewModel
import com.example.tubesppb.viewmodel.BookViewModelFactory

class BookmarkActivity : AppCompatActivity() {
    private lateinit var recyclerMark: RecyclerView
    private lateinit var markAdapter: BookFavoriteAdapter
    private lateinit var btnSearchBookmark: Button
    private lateinit var searchBookmark: EditText
    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as Application).bookRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        btnSearchBookmark = findViewById(R.id.buttonBookmark)
        searchBookmark = findViewById(R.id.inputBookmark)
        recyclerMark = findViewById(R.id.bookBookmarkView)

        markAdapter = BookFavoriteAdapter(this)
        recyclerMark.adapter = markAdapter
        recyclerMark.layoutManager = LinearLayoutManager(this)

        bookViewModel.allBooks.observe(this, { books ->
            books?.let { markAdapter.submitList(it) }
        })

        btnSearchBookmark.setOnClickListener {
            recyclerMark = findViewById(R.id.bookBookmarkView)

            markAdapter = BookFavoriteAdapter(this)
            recyclerMark.adapter = markAdapter
            recyclerMark.layoutManager = LinearLayoutManager(this)

            bookViewModel.certainBooks(searchBookmark.text.toString()).observe(this, { books ->
                books?.let { markAdapter.submitList(it) }
            })
        }
    }
}