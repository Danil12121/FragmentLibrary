package com.example.libraryui.presentation.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.R
import com.example.libraryui.databinding.LibItemBinding
import com.example.libraryui.domain.models.Book

class GoogleBookHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = LibItemBinding.bind(viewItem)
    var itemName = binding.tvItemName
    val itemID = binding.tvItemID
    var itemImage = binding.ivItemImage
    fun bind(item: Book) {
        binding.apply {
            itemImage.setImageResource(R.drawable.book)
            itemName.text = item.title
            itemID.text = "ID: ${item.id}"
        }
    }
}