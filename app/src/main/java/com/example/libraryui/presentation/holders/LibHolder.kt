package com.example.libraryui.presentation.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.databinding.LibItemBinding

class LibHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = LibItemBinding.bind(viewItem)
    var itemName = binding.tvItemName
    val itemID = binding.tvItemID
    var itemImage = binding.ivItemImage

    fun bind(item: LibraryItem) {
        binding.apply {
            itemImage.setImageResource(item.imageID)
            itemName.text = item.title
            itemID.text = "ID: ${item.id}"
        }
    }
}