package com.example.libraryui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class GoogleBookAdapter : ListAdapter<Book, GoogleBookHolder>(GoogleBookDiffCallback()){
    private var onItemLongClickListener: ((Book) -> Boolean)? = null
    private var onItemClickListener: ((Book) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoogleBookHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lib_item, parent, false
        )
        return GoogleBookHolder(view)
    }


    override fun onBindViewHolder(holder: GoogleBookHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(item) == true
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnLongClickListener(listener: (Book) -> Boolean) {
        onItemLongClickListener = listener
    }
    fun setOnClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }
}