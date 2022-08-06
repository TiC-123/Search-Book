package com.example.tubesppb.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesppb.Application
import com.example.tubesppb.R
import com.example.tubesppb.presenter.Request
import com.example.tubesppb.adapter.BookSearchAdapter
import com.example.tubesppb.model.BookModel
import com.example.tubesppb.presenter.Search
import com.example.tubesppb.viewmodel.BookViewModel
import com.example.tubesppb.viewmodel.BookViewModelFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

class SearchActivity : AppCompatActivity() {
    private var searchQuery: String? = null
    private lateinit var search: Search
    private lateinit var searchEdit: EditText
    private lateinit var searchBtn: Button
    private lateinit var bookData: ArrayList<BookModel>
    private lateinit var recyclerSearch: RecyclerView
    private lateinit var searchAdapter: BookSearchAdapter

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as Application).bookRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val size = sharedPreferences.getInt("SIZE_KEY", 25)
        search = Search(this)
        bookData = ArrayList<BookModel>()
        recyclerSearch = findViewById(R.id.bookRecyclerView)
        searchEdit = findViewById(R.id.inputSearch)
        searchBtn = findViewById(R.id.buttonSearch)
        recyclerSearch.layoutManager = LinearLayoutManager(this)

        searchQuery = search.getQuery(intent.getStringExtra("search").toString(), size)
        searchView()

        searchBtn.setOnClickListener {
            searchQuery = search.getQuery(searchEdit.text.toString(), size)
            bookData.clear()

            doAsync {
                if (!Request().checkConnection(this@SearchActivity)) {
                    uiThread {
                        Toast.makeText(this@SearchActivity, "No Internet Connection!!!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    uiThread {
                        searchView()
                    }
                }
            }
        }
    }

    private fun searchView() {
        if (searchQuery == "") {
            Toast.makeText(this, "Fill the Query!!!", Toast.LENGTH_SHORT).show()
        } else {
            doAsync {
                bookData = search.parseJson(searchQuery.toString(), bookViewModel)

                uiThread {
                    searchAdapter = BookSearchAdapter(bookData, this@SearchActivity)
                    recyclerSearch.adapter = searchAdapter
                }
            }
        }
    }
}