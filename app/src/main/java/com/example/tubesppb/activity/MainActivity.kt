package com.example.tubesppb.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesppb.*
import com.example.tubesppb.adapter.BookFavoriteAdapter
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

class MainActivity: AppCompatActivity() {
    private var searchQuery: String? = null
    private lateinit var recyclerFav: RecyclerView
    private lateinit var recyclerRec: RecyclerView
    private lateinit var search: Search
    private lateinit var recommendedData: ArrayList<BookModel>
    private lateinit var someRecommended: ArrayList<BookModel>
    private lateinit var favAdapter: BookFavoriteAdapter
    private lateinit var recAdapter: BookRecommendAdapter
    private lateinit var searchBtn: Button
    private lateinit var searchEdit: EditText
    private lateinit var seeBookmark: TextView
    private lateinit var seeRecommend: TextView

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as Application).bookRepository)
    }

    private val recommendViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory((application as Application).historyRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val size = sharedPreferences.getInt("SIZE_KEY", 25)
        var query: String

        search = Search(this)
        searchBtn = findViewById(R.id.buttonCari)
        searchEdit = findViewById(R.id.inputText)
        seeBookmark = findViewById(R.id.lihatBookmark)
        seeRecommend = findViewById(R.id.lihatRekomendasi)
        recommendedData = ArrayList<BookModel>()
        someRecommended = ArrayList<BookModel>()

        recyclerFav = findViewById(R.id.bookFavoriteView)
        favAdapter = BookFavoriteAdapter(this)
        recyclerFav.adapter = favAdapter
        recyclerFav.layoutManager = LinearLayoutManager(this)

        recyclerRec = findViewById(R.id.bookRecommendedView)
        recyclerRec.layoutManager = LinearLayoutManager(this)


        //Menampilkan bookmark dan rekomendasi recycler view
        bookViewModel.someBooks.observe(this, { books ->
            books?.let { favAdapter.submitList(it) }
        })

        recommendViewModel.allHistory.observe(this@MainActivity, { history ->
            if (history.isNotEmpty()) {
                searchQuery = history[0].category
                query = search.getQuery(searchQuery!!, size)

                showRecommended(query)
            }
        })
        //

        seeBookmark.setOnClickListener {
            startActivity(Intent(this, BookmarkActivity::class.java))
        }

        seeRecommend.setOnClickListener {
            startActivity(Intent(this, RecommendedActivity::class.java))
        }

        searchBtn.setOnClickListener {
            doAsync {
                if (!Request().checkConnection(this@MainActivity)) {
                    uiThread {
                        Toast.makeText(this@MainActivity, "No Internet Connection!!!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    uiThread {
                        mainView()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting->{
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecommended(query: String) {
        recommendedData.clear()
        someRecommended.clear()

        doAsync {
            recommendedData = search.parseJson(query, bookViewModel)

            for (i in 0 until 5) {
                someRecommended.add(recommendedData[i])
            }

            uiThread {
                recAdapter = BookRecommendAdapter(someRecommended, this@MainActivity)
                recyclerRec.adapter = recAdapter
            }
        }
    }

    private fun mainView() {
        val searchQuery = searchEdit.text.toString()
        val intent = Intent(this, SearchActivity::class.java)

        if (searchQuery == "") {
            Toast.makeText(this, "Fill the Query!!!", Toast.LENGTH_SHORT).show()
        } else {
            intent.putExtra("search", searchQuery)
            startActivity(intent)
        }
    }

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/volumes?q="
    }
}