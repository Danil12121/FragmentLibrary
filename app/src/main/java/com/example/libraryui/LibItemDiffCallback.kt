package com.example.libraryui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class LibItemDiffCallback : DiffUtil.ItemCallback<LibraryItem>() {

    override fun areItemsTheSame(
        oldItem: LibraryItem, newItem: LibraryItem
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: LibraryItem, newItem: LibraryItem
    ): Boolean {
        if (oldItem.javaClass != newItem.javaClass) return false
        return oldItem == newItem
    }

}