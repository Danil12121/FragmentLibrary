package com.example.libraryui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.databinding.LibItemBinding
import kotlin.Int
import kotlin.properties.Delegates

class LibAdapter : RecyclerView.Adapter<LibAdapter.LibHolder>() {
    val libList = mutableListOf<LibraryItem>()

    class LibHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val binding = LibItemBinding.bind(viewItem)
        val cardView: CardView = viewItem.findViewById(R.id.cardView)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemID: TextView = itemView.findViewById(R.id.itemID)

        var itID by Delegates.notNull<Int>()
        val dpCONST = viewItem.context.resources.displayMetrics.density
        fun bind(item: LibraryItem) {
            binding.apply {
                itID = item.id
                itemImage.setImageResource(item.imageID)
                itemName.text = item.name
                itemID.text = "ID: ${item.id}"
            }
            itemName.alpha = if (item.isEnable) 1.0f else 0.3f
            itemID.alpha = if (item.isEnable) 1.0f else 0.3f
            cardView.elevation = if (item.isEnable) 10 * dpCONST else 1 * dpCONST
            cardView.translationZ = if (item.isEnable) 10 * dpCONST else 1 * dpCONST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lib_item,
            parent, false
        )
        return LibHolder(view)
    }

    override fun onBindViewHolder(holder: LibHolder, position: Int) {
        val item = libList[position]
        holder.bind(item)
        holder.cardView.setOnClickListener {
            item.isEnable = !item.isEnable
            notifyItemChanged(position)
            Toast.makeText(
                holder.itemView.context,
                "Элемент с id ${holder.itID}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount() = libList.size

    fun notify(position: Int) {
        libList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, libList.size - position)
    }


}
