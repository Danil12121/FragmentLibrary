package com.example.libraryui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlin.Int

class LibAdapter() : ListAdapter<LibraryItem, LibHolder>(LibItemDiffCallback()) {
    private var onItemClickListener: ((LibraryItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lib_item, parent, false
        )
        return LibHolder(view)
    }

    override fun onBindViewHolder(holder: LibHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(listener: (LibraryItem) -> Unit) {
        onItemClickListener = listener
    }
}