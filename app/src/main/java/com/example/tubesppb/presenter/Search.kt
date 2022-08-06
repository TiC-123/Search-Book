package com.example.tubesppb.presenter

import android.content.Context
import android.net.Uri
import com.example.tubesppb.activity.MainActivity
import com.example.tubesppb.model.BookModel
import com.example.tubesppb.viewmodel.BookViewModel
import java.util.ArrayList

class Search(val context: Context) {
    fun getQuery(query: String, size: Int): String {
        return if (query == "") {
            ""
        } else {
            val searchQuery = query.replace(" ", "%20")
            val uri = Uri.parse(MainActivity.BASE_URL + searchQuery + "&maxResults=" + size.toString())
            val builder = uri.buildUpon()

            builder.toString()
        }
    }

    fun parseJson(key: String, bookViewModel: BookViewModel): ArrayList<BookModel> {
        var title = ""; var author = ""; var desc = ""; var image = ""
        var category = ""; var buy = ""; var preview = ""; var heart = false
        val items = Request().run(key); val bookData = ArrayList<BookModel>()

        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            val volumeInfo = item.getJSONObject("volumeInfo")
            val saleInfo = item.getJSONObject("saleInfo")

            //Log.d("book$i", volumeInfo.getString("title"))

            try {
                title = volumeInfo.getString("title")
                desc = volumeInfo.getString("description")
                heart = bookViewModel.check(title)

                val authors = volumeInfo.getJSONArray("authors")
                author += if (authors.length() == 1) {
                    authors.getString(0)
                } else {
                    var j = 0
                    var neff = ""
                    while (j < authors.length()) {
                        neff += authors.getString(j)
                        j++
                        if (j != authors.length()) {
                            neff += " ~ "
                        }
                    }
                    neff
                }

                val categories = volumeInfo.getJSONArray("categories")
                category += if (categories.length() == 0) {
                    "other"
                } else {
                    categories.getString(0)
                }

                image = volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                buy = saleInfo.getString("buyLink")
                preview = volumeInfo.getString("previewLink")
            } catch (e: Exception) {}

            bookData.add(
                BookModel(title, author, desc, category, image, buy, preview, heart)
            )
        }

        return bookData
    }
}