package com.example.tubesppb.activity

import android.content.Context
import android.content.Intent
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
import com.example.tubesppb.adapter.BookRecommendAdapter
import com.example.tubesppb.model.BookModel
import com.example.tubesppb.presenter.Request
import com.example.tubesppb.presenter.Search
import com.example.tubesppb.viewmodel.BookViewModel
import com.example.tubesppb.viewmodel.BookViewModelFactory
import com.example.tubesppb.viewmodel.HistoryViewModel
import com.example.tubesppb.viewmodel.HistoryViewModelFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

class RecommendedActivity: AppCompatActivity() {
    private var searchQuery: String? = null
    private lateinit var searchBtn: Button
    private lateinit var inputSearch: EditText
    private lateinit var recyclerRec: RecyclerView
    private lateinit var recAdapter: BookRecommendAdapter
    private lateinit var recommendedData: ArrayList<BookModel>
    private lateinit var search: Search

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as Application).bookRepository)
    }

    private val recommendViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory((application as Application).historyRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended)

        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val size = sharedPreferences.getInt("SIZE_KEY", 25)
        var query: String
        search = Search(this)
        searchBtn = findViewById(R.id.searchButtonBook)
        inputSearch = findViewById(R.id.inputRec)
        recommendedData = ArrayList<BookModel>()
        recyclerRec = findViewById(R.id.bookRekomendasiView)
        recyclerRec.layoutManager = LinearLayoutManager(this)

        recommendViewModel.allHistory.observe(this, { history ->
            if (history.isNotEmpty()) {
                searchQuery = history[0].category
                query = search.getQuery(searchQuery!!, size)

                showRecommended(query)
            }
        })

        searchBtn.setOnClickListener {
            doAsync {
                if (!Request().checkConnection(this@RecommendedActivity)) {
                    uiThread {
                        Toast.makeText(this@RecommendedActivity, "No Internet Connection!!!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    uiThread {
                        searchBook()
                    }
                }
            }
        }
    }

    private fun searchBook() {
        val searchQuery = inputSearch.text.toString()
        val intent = Intent(this, SearchActivity::class.java)

        if (searchQuery == "") {
            Toast.makeText(this, "Fill the Query!!!", Toast.LENGTH_SHORT).show()
        } else {
            intent.putExtra("search", searchQuery)
            startActivity(intent)
        }
    }

    private fun showRecommended(query: String) {
        recommendedData.clear()

        doAsync {
            recommendedData = search.parseJson(query, bookViewModel)

            uiThread {
                recAdapter = BookRecommendAdapter(recommendedData, this@RecommendedActivity)
                recyclerRec.adapter = recAdapter
            }
        }
    }
}