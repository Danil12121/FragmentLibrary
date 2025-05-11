package com.example.libraryui.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.libraryui.presentation.holders.LibHolder
import com.example.libraryui.common.utils.LibItemDiffCallback
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.R
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