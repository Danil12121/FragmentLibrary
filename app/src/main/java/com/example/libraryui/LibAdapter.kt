package com.example.libraryui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.Int

class LibAdapter : RecyclerView.Adapter<LibHolder>() {
    var libList = mutableListOf<LibraryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lib_item,
            parent,
            false
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

    fun deleteAfterSwap(position: Int) {
        libList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, libList.size - position)
    }
}