package com.example.libraryui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.databinding.LibItemBinding

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