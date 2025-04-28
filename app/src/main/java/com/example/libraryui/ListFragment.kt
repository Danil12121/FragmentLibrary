package com.example.libraryui


import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.libraryui.BookDao
import com.example.libraryui.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.random.Random

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: LibAdapter
    private lateinit var viewModel: MainViewModel
    private var itemClickListener: ((LibraryItem) -> Unit)? = null
    private var countOfItem = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val database = Room.databaseBuilder(requireContext(), LibDatabase::class.java, "library.db").build()
        val repository = LibraryRepository(database.bookDao(), database.diskDao(), database.newspaperDao(), prefs)
        viewModel = MainViewModel(repository)
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = LibAdapter().apply {
            setOnItemClickListener { item ->
                handleItemClick(item)
            }
        }

        binding.rcvLibraryItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ListFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.loadNextPage()
                    }

                    if (layoutManager.findFirstVisibleItemPosition() <= 10) {
                        viewModel.loadPreviousPage()
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is State.Loading -> showShimmer()
                        is State.Success -> {
                            hideShimmer()
                            viewModel.addItem(state.data[countOfItem])
                            adapter.submitList(state.data)
                        }
                        is State.Error -> {
                            hideShimmer()
                            showError(state.message)
                        }
                    }
                }
            }
        }

        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            item?.let {
                navigateToDetails(it)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            buttonTitle.setOnClickListener {
                viewModel.setSortOrder("title")
            }

            buttonDate.setOnClickListener {
                viewModel.setSortOrder("date")
            }

            buttonAdd.setOnClickListener {
                navigateToAddNewItem()
            }
        }
    }

    private fun handleItemClick(item: LibraryItem) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            itemClickListener?.invoke(item)
        } else {
            viewModel.selectItem(item)
        }
    }

    private fun navigateToDetails(item: LibraryItem) {
        viewModel.messageToFullInfo.value = "OLD"
        viewModel.setItemToFullInfo(item)
        findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
    }

    private fun navigateToAddNewItem() {
        viewModel.messageToFullInfo.value = "NEW"
        viewModel.idToFullInfo.value = countOfItem++
            findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
    }

    private fun showShimmer() {
        binding.apply {
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            rcvLibraryItems.visibility = View.GONE
        }
    }

    private fun hideShimmer() {
        binding.apply {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            rcvLibraryItems.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, 2500).show()
    }

    fun setOnItemClickListener(listener: (LibraryItem) -> Unit) {
        itemClickListener = listener
    }
}