package com.example.tubesppb.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tubesppb.*
import com.example.tubesppb.database.Book
import com.example.tubesppb.Application
import com.example.tubesppb.database.History
import com.example.tubesppb.viewmodel.BookViewModel
import com.example.tubesppb.viewmodel.BookViewModelFactory
import com.example.tubesppb.viewmodel.HistoryViewModel
import com.example.tubesppb.viewmodel.HistoryViewModelFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailActivity : AppCompatActivity() {
    private val options: RequestOptions = RequestOptions().centerCrop().placeholder(R.drawable.ic_baseline_load).error(R.drawable.ic_baseline_close)

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as Application).bookRepository)
    }

    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory((application as Application).historyRepository)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = findViewById<TextView>(R.id.titleDetail)
        val author = findViewById<TextView>(R.id.authorDetail)
        val desc = findViewById<TextView>(R.id.descDetail)
        val img = findViewById<ImageView>(R.id.bookCover)
        val check = findViewById<ImageView>(R.id.check)
        val buy = findViewById<Button>(R.id.buyButton)
        val preview = findViewById<Button>(R.id.previewButton)

        title.text = intent.getStringExtra("title")
        author.text = intent.getStringExtra("author")
        desc.text = intent.getStringExtra("desc")
        Glide.with(this).load(intent.getStringExtra("image").toString()).apply(options).into(img)

        doAsync {
            val heart = bookViewModel.check(title.text.toString())
            val cat = intent.getStringExtra("category")

            if (cat != null) {
                historyViewModel.delHistory()
                historyViewModel.insert(History(cat))
            }

            if (intent.getBooleanExtra("check", false) || heart) {
                uiThread {
                    check.setImageResource(R.drawable.heart_check)
                }
            }
        }

        check.setOnClickListener {
            if (
                (check.drawable as BitmapDrawable).bitmap.toString() ==
                (resources.getDrawable(R.drawable.unfavorite,theme) as BitmapDrawable).bitmap.toString()
            ) {
                check.setImageResource(R.drawable.heart_check)

                doAsync {
                    inputData(title.text.toString(), author.text.toString(), desc.text.toString())
                }
            } else {
                check.setImageResource(R.drawable.unfavorite)

                doAsync {
                    deleteData(title.text.toString())
                }
            }
        }

        buy.setOnClickListener {
            if (intent.getStringExtra("buy").isNullOrEmpty()) {
                Toast.makeText(this, "currently unavailable!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("buy"))))
            }
        }

        preview.setOnClickListener {
            if (intent.getStringExtra("preview").isNullOrEmpty()) {
                Toast.makeText(this, "preview unavailable!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("preview"))))
            }
        }
    }

    private fun inputData(title: String, author: String, desc: String) {
        val item = Array(6) {"it = $it"}
        item[0] = title
        item[1] = author
        item[2] = desc
        item[3] = intent.getStringExtra("image").toString()
        item[4] = intent.getStringExtra("buy").toString()
        item[5] = intent.getStringExtra("preview").toString()

        val book = Book(item[0], item[1], item[2], item[3], item[4], item[5])

        bookViewModel.insert(book)
    }

    private fun deleteData(title: String) {
        bookViewModel.delBook(title)
    }
}