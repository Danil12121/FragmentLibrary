package com.example.libraryui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
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

    val callback = SwipeCallback(adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rcView = binding.rcvLibraryItems
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter
        ItemTouchHelper(callback).attachToRecyclerView(rcView)

        init()
    }

    private fun init() = with(binding) {
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