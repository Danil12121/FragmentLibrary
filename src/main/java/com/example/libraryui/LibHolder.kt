package com.example.libraryui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.databinding.LibItemBinding
import kotlin.properties.Delegates

class LibHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = LibItemBinding.bind(viewItem)
    var itemName = binding.tvItemName
    val itemID = binding.tvItemID
    var itemImage = binding.ivItemImage

    fun bind(item: LibraryItem) {
        binding.apply {
            itemImage.setImageResource(item.imageID)
            itemName.text = item.name
            itemID.text = "ID: ${item.id}"
        }
    }
}