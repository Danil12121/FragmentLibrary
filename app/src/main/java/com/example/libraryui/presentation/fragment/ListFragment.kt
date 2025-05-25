package com.example.libraryui.presentation.fragment


import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.libraryui.presentation.adapters.GoogleBookAdapter
import com.example.libraryui.presentation.adapters.LibAdapter
import com.example.libraryui.data.database.LibDatabase
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.R
import com.example.libraryui.common.utils.State
import com.example.libraryui.data.repository.LibraryRepositoryImpl
import com.example.libraryui.databinding.FragmentListBinding
import com.example.libraryui.domain.models.Book
import com.example.libraryui.presentation.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.example.libraryui.domain.usecase.Interactor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: LibAdapter
    private lateinit var adapterGoogle: GoogleBookAdapter
    val viewModel: MainViewModel by activityViewModels()
    private var itemClickListener: ((LibraryItem) -> Unit)? = null
    private var countOfItem = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        val recyclerView = binding.rcvLibraryItems
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = LibAdapter()

        val recyclerViewGoogle = binding.rcvGoogleBooks
        recyclerViewGoogle.layoutManager = LinearLayoutManager(requireContext())
        adapterGoogle = GoogleBookAdapter()

        setupRecyclerView()
        recyclerView.adapter = adapter
        recyclerViewGoogle.adapter = adapterGoogle
        setupObservers()
        setupListeners()
        showGoogleBooks(View.INVISIBLE)
    }

    private fun setupRecyclerView() {
        adapter = LibAdapter().apply {
            setOnItemClickListener { item ->
                handleItemClick(item)
            }
        }

        adapterGoogle = GoogleBookAdapter().apply {
            setOnLongClickListener { book ->
                viewModel.addItem(book)
                Snackbar.make(binding.root, "книга с id${book.id} добавлена в библиотеку", 2500)
                    .show()
                return@setOnLongClickListener true
            }
            setOnClickListener { book ->
                Snackbar.make(binding.root, "это обычный, а не длинный клик", 2500)
                    .show()
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
                        is State.Loading -> {
                            showShimmer(View.VISIBLE, View.INVISIBLE)
                            hideProgressBar()
                            binding.shimmerLayout.startShimmer()
                        }
                        is State.Success -> {
                            showShimmer(View.INVISIBLE, View.VISIBLE)
                            binding.shimmerLayout.stopShimmer()
                            hideProgressBar()
                            if (state.data.isEmpty()) Snackbar.make(
                                binding.root,
                                "Список сейчас пуст",
                                3000
                            ).show()
                            else adapter.submitList(state.data)
                            hideProgressBar()
                        }

                        is State.Error -> {
                            showShimmer(View.INVISIBLE, View.VISIBLE)
                            binding.shimmerLayout.stopShimmer()
                            hideProgressBar()
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateGoogle.collect { state ->
                    when (state) {
                        is State.Loading -> {
                            showShimmer(View.VISIBLE, View.INVISIBLE)
                            binding.shimmerLayout.startShimmer()
                        }
                        is State.Success -> {
                            showShimmer(View.INVISIBLE, View.INVISIBLE)
                            binding.shimmerLayout.stopShimmer()
                            if (state.data.isEmpty()) Snackbar.make(
                                binding.root,
                                "Список сейчас пуст",
                                3000
                            ).show()
                            else adapterGoogle.submitList(state.data as List<Book?>?)
                        }

                        is State.Error -> {
                            showShimmer(View.INVISIBLE, View.VISIBLE)
                            binding.shimmerLayout.stopShimmer()
                            showError(state.message)
                        }
                    }
                }
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
            buttonMyLibrary.setOnClickListener {
                showGoogleBooks(View.INVISIBLE)
                showMyLibrary(View.VISIBLE)
            }
            buttonGoogleBooks.setOnClickListener {
                showMyLibrary(View.INVISIBLE)
                showGoogleBooks(View.VISIBLE)
            }
            buttonSearch.setOnClickListener {
                val author = binding.etAuthorName.text.toString()
                val title = binding.etBookName.text.toString()
                if (author.length > 2 || title.length > 2) {
                    viewModel.searchBooks(author.trim(), title.trim())
                }
            }
        }
    }

    private fun showMyLibrary(flag: Int) {
        binding.rcvLibraryItems.visibility = flag
        binding.buttonDate.visibility = flag
        binding.buttonTitle.visibility = flag
        binding.buttonAdd.visibility = flag
    }

    private fun showGoogleBooks(flag: Int) {
        binding.rcvGoogleBooks.visibility = flag
        binding.buttonSearch.visibility = flag
        binding.etBookName.visibility = flag
        binding.etAuthorName.visibility = flag
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

    private fun showShimmer(flagShim: Int, flagRcv: Int) {
        binding.shimmerLayout.visibility = flagShim
        binding.rcvLibraryItems.visibility = flagRcv
    }

    private fun hideProgressBar() {
        binding.myProgressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, 2500).show()
    }

    fun setOnItemClickListener(listener: (LibraryItem) -> Unit) {
        itemClickListener = listener
    }
}