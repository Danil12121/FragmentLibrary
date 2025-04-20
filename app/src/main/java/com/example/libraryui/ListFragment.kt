package com.example.libraryui


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryui.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.random.Random

class ListFragment : Fragment(R.layout.fragment_list) {
    val INIT_FLAG_STR = "initFlag"
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: LibAdapter
    private val mainViewModel: MainViewModel by activityViewModels()
    private var countOfItem = 0
    private val currentItems = mutableListOf<LibraryItem>()
    private var initFlag = true
    private var itemClickListener: ((LibraryItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (LibraryItem) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    fun firstInit(mainViewModel: MainViewModel) {
        if (initFlag) {
            currentItems.addAll(mainViewModel.initList)
            adapter.submitList(currentItems.toList())
            initFlag = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(INIT_FLAG_STR, initFlag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            initFlag = it.getBoolean(INIT_FLAG_STR, true)
        }
        adapter = LibAdapter()

        binding.rcvLibraryItems.adapter = adapter
        binding.rcvLibraryItems.layoutManager = LinearLayoutManager(requireContext())
        firstInit(mainViewModel)

        lifecycleScope.launch {
            mainViewModel.loadDataToListFragment(adapter)
        }

        lifecycleScope.launch {
            mainViewModel.items.collect { adapter.submitList(it) }
        }
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is State.Loading -> showShimmer()
                    is State.Success -> {
                        hideShimmer()
                        binding.rcvLibraryItems.visibility = View.VISIBLE
                    }

                    is State.Error -> showError(it.message)
                }
            }
        }
        setListeners()

        mainViewModel.itemToList.observe(viewLifecycleOwner) { newItem ->
            if (currentItems.none { it.id == newItem.id }) {
                currentItems.add(newItem)
                adapter.submitList(currentItems.toList())
                binding.rcvLibraryItems.scrollToPosition(currentItems.size - 1)
            }
        }
    }

    fun setListeners() {
        adapter.setOnItemClickListener { item ->
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                itemClickListener?.invoke(item)
            } else {
                mainViewModel.messageToFullInfo.value = "OLD"
                mainViewModel.setItemToFullInfo(item)
                lifecycleScope.launch {
                    imitationOfLoad(item)
                }
            }
        }

        binding.buttonAdd.setOnClickListener {
            mainViewModel.messageToFullInfo.value = "NEW"
            mainViewModel.idToFullInfo.value = countOfItem++
            findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
        }
    }

    suspend fun imitationOfLoad(item: LibraryItem) {
        var flag = true
        try {
            showShimmer()
            showProgressBar()
            delay(Random.nextLong(100, 2000))
            if (Random.nextInt(1, 5) == 3) {
                throw Exception()
            }
        } catch (e: Exception) {
            hideShimmer()
            binding.rcvLibraryItems.visibility = View.VISIBLE
            mainViewModel._state.value = State.Error(
                e.message ?: "Ошибка при получении данных об элементе с id ${item.id}"
            )
            flag = false
        }
        if (flag) {
            hideShimmer()
            findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
        }
    }

    private fun showShimmer() {
        hideProgressBar()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.rcvLibraryItems.visibility = View.GONE
    }

    private fun showProgressBar() {
        hideShimmer()
        binding.myProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.myProgressBar.visibility = View.GONE
    }

    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        hideProgressBar()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, 2500).show()
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(currentItems.toList())
    }
}