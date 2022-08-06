package com.example.tubesppb.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tubesppb.R
import com.example.tubesppb.activity.DetailActivity
import com.example.tubesppb.database.Book

class BookFavoriteAdapter(private val context: Context): ListAdapter<Book, BookFavoriteAdapter.BookViewHolder>(BooksComparator()) {
    private val contextHere = context
    private val options: RequestOptions =
        RequestOptions().centerCrop().placeholder(R.drawable.ic_baseline_load).error(R.drawable.ic_baseline_close)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current.title, current.author, current.description)

        Glide.with(contextHere).load(current.image).apply(options).into(holder.imageView)

        holder.detail.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", current.title)
            intent.putExtra("author",current.author)
            intent.putExtra("desc", current.description)
            intent.putExtra("image", current.image)
            intent.putExtra("check", true)
            intent.putExtra("buy", current.buy)
            intent.putExtra("preview", current.preview)
            context.startActivity(intent)
        }

        holder.heart.setImageResource(R.drawable.heart_check)
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        companion object {
            fun create(parent: ViewGroup): BookViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.book_recycler, parent, false)

                return BookViewHolder(view)
            }
        }
    }

    class BooksComparator : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.title == newItem.title
        }
    }
}