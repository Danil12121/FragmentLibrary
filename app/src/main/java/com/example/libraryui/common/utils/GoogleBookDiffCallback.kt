package com.example.libraryui.common.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.libraryui.domain.models.Book

class GoogleBookDiffCallback  : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}