package com.example.libraryui


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryui.databinding.FragmentListBinding
import kotlin.getValue


class ListFragment : Fragment(R.layout.fragment_list) {
    lateinit var binding: FragmentListBinding
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

    fun firstInit() {
        if (initFlag) {
            val initialItems = listOf(
                Book(100, "dsasd", "String", 1),
                Newspaper(1500, "news2", 19),
                Disk(11111, "wqew", "CD"),
                Disk(2222, "wedaaa", "DVD"),
                Book(10101, "MYBOOK", "AUTHOR", 234),
                Newspaper(15002, "2news22", 14569),
                Newspaper(15003, "3news22", 1934)
            )

            currentItems.addAll(initialItems)
            adapter.submitList(currentItems.toList())
            initFlag = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("initFlag", initFlag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            initFlag = it.getBoolean("initFlag", true)
        }
        adapter = LibAdapter()
        binding.rcvLibraryItems.adapter = adapter
        binding.rcvLibraryItems.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener { item ->
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                itemClickListener?.invoke(item)
            } else {
                mainViewModel.messageToFullInfo.value = "OLD"
                mainViewModel.setItemToFullInfo(item)
                findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
            }
        }

        firstInit()
        mainViewModel.itemToList.observe(viewLifecycleOwner) { newItem ->
            if (currentItems.none { it.id == newItem.id }) {
                currentItems.add(newItem)
                adapter.submitList(currentItems.toList())
                binding.rcvLibraryItems.scrollToPosition(currentItems.size - 1)
            }
        }

        binding.buttonAdd.setOnClickListener {
            mainViewModel.messageToFullInfo.value = "NEW"
            mainViewModel.idToFullInfo.value = countOfItem++
            findNavController().navigate(R.id.action_listFragment_to_fullInfoFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(currentItems.toList())
    }
}