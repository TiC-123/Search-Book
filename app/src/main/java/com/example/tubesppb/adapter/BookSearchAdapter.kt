package com.example.tubesppb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesppb.R
import com.example.tubesppb.activity.DetailActivity
import com.example.tubesppb.activity.SearchActivity
import com.example.tubesppb.model.BookModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BookSearchAdapter(private val mBook: List<BookModel>, private val context: SearchActivity): RecyclerView.Adapter<BookSearchAdapter.ViewHolder>() {
    private val contextHere = context
    private val options: RequestOptions =
        RequestOptions().centerCrop().placeholder(R.drawable.ic_baseline_load).error(R.drawable.ic_baseline_close)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_recycler, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookModel = mBook[position]

        holder.bind(bookModel.title, bookModel.author, bookModel.description)

        Glide.with(contextHere).load(bookModel.image).apply(options).into(holder.imageView)

        holder.detail.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", bookModel.title)
            intent.putExtra("author", bookModel.author)
            intent.putExtra("desc", bookModel.description)
            intent.putExtra("image", bookModel.image)
            intent.putExtra("buy", bookModel.buyLink)
            intent.putExtra("preview", bookModel.previewLink)
            intent.putExtra("category", bookModel.category)

            context.startActivity(intent)
        }

        if (bookModel.inDb) {
            holder.heart.setImageResource(R.drawable.heart_check)
        } else {
            holder.heart.setImageResource(R.drawable.unfavorite)
        }
    }

    override fun getItemCount(): Int {
        return mBook.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.textJudul)
        private val authorView: TextView = itemView.findViewById(R.id.textPenulis)
        private val descView: TextView = itemView.findViewById(R.id.textDeskripsi)
        val imageView: ImageView = itemView.findViewById(R.id.coverBuku)
        val detail: Button = itemView.findViewById(R.id.buttonBeli)
        val heart: ImageView = itemView.findViewById(R.id.heart)

        fun bind(title: String?, author: String?, desc: String?) {
            titleView.text = title
            authorView.text = author
            descView.text = desc
        }
    }
}