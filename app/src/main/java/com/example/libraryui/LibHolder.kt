package com.example.libraryui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.databinding.LibItemBinding
import kotlin.properties.Delegates

class LibHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
    val binding = LibItemBinding.bind(viewItem)
    val cardView = binding.cvLibraryItem
    val itemName = binding.tvItemName
    val itemID = binding.tvItemID
    val itemImage = binding.ivItemImage
    var itID by Delegates.notNull<Int>()
    val dpCONST = viewItem.context.resources.displayMetrics.density

    companion object {
        const val AVAILABLE_ITEM_ALPHA = 1.0f
        const val TAKEN_ITEM_ALPHA = 0.3f
    }

    fun bind(item: LibraryItem) {
        binding.apply {
            itID = item.id
            itemImage.setImageResource(item.imageID)
            itemName.text = item.name
            itemID.text = "ID: ${item.id}"
        }
        itemName.alpha = if (item.isEnable) AVAILABLE_ITEM_ALPHA else TAKEN_ITEM_ALPHA
        itemID.alpha = if (item.isEnable) AVAILABLE_ITEM_ALPHA else TAKEN_ITEM_ALPHA
        cardView.elevation = if (item.isEnable) 10 * dpCONST else 1 * dpCONST
        cardView.translationZ = if (item.isEnable) 10 * dpCONST else 1 * dpCONST
    }
}