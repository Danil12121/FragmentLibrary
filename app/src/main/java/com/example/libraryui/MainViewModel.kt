package com.example.libraryui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _itemToList = MutableLiveData<LibraryItem>()
    val itemToList: LiveData<LibraryItem> = _itemToList

    private val _itemToFullInfo = MutableLiveData<LibraryItem?>(null)
    val itemToFullInfo: LiveData<LibraryItem?> = _itemToFullInfo

    val messageToFullInfo = MutableLiveData<String>()
    val idToFullInfo = MutableLiveData<Int>()
    var currentItem = MutableLiveData<LibraryItem>()

    fun setItemToList(item: LibraryItem) {
        _itemToList.value = item
    }

    fun setItemToFullInfo(item: LibraryItem) {
        _itemToFullInfo.value = item
    }

    fun clearCurrentItem() {
        currentItem.value = null
    }

    fun setCurrentItem(item: LibraryItem) {
        currentItem.value = item
    }

    val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _items = MutableStateFlow<List<LibraryItem>>(emptyList())
    val items: StateFlow<List<LibraryItem>> = _items.asStateFlow()
    val initList = listOf(
        Book(100, "dsasd", "String", 1),
        Newspaper(1500, "news2", 19),
        Disk(11111, "wqew", "CD"),
        Disk(2222, "wedaaa", "DVD"),
        Book(10101, "MYBOOK", "AUTHOR", 234),
        Newspaper(15002, "2news22", 14569),
        Newspaper(15003, "3news22", 1934)
    )

    suspend fun loadDataToListFragment(adapter: LibAdapter) {
        viewModelScope.launch {
            _state.value = State.Loading
            delay(1000)
            _items.value = adapter.currentList
            _state.value = State.Success(_items.value)
        }
    }
}
