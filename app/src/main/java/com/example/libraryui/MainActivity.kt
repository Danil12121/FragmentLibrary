package com.example.libraryui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryui.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = LibAdapter()
    private val imageIDList = listOf(
        R.drawable.book,
        R.drawable.newspaper,
        R.drawable.disk
    )
    private val nameList = listOf(
        "Book",
        "Newspaper",
        "Disk"
    )

    val callback = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
        ) = makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)


        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.notify(viewHolder.bindingAdapterPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rcView = binding.rcView
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter
        ItemTouchHelper(callback).attachToRecyclerView(rcView)

        init()
    }

    private fun init() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        rcView.adapter = adapter
        for (i: Int in 0..1000) {
            adapter.libList.add(
                LibraryItem(
                    imageID = imageIDList[i % 3],
                    id = i,
                    isEnable = true,
                    name = nameList[i % 3]
                )
            )
        }
    }
}