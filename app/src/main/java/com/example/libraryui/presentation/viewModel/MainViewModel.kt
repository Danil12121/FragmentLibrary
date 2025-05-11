package com.example.libraryui.presentation.viewModel

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryui.data.repository.LibraryRepositoryImpl
import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.domain.models.Book
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.common.utils.State
import com.example.libraryui.domain.usecase.Interactor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.Serializable

class MainViewModel(
    private val interactor: Interactor
) : ViewModel(), Serializable {

    private val _itemToFullInfo = MutableLiveData<LibraryItem?>(null)
    val itemToFullInfo: LiveData<LibraryItem?> = _itemToFullInfo

    val messageToFullInfo = MutableLiveData<String>()
    val idToFullInfo = MutableLiveData<Int>()
    var currentItem = MutableLiveData<LibraryItem>()

    private val _selectedItem = MutableLiveData<LibraryItem?>(null)
    val selectedItem: LiveData<LibraryItem?> = _selectedItem

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _stateGoogle = MutableStateFlow<State>(State.Loading)
    val stateGoogle: StateFlow<State> = _stateGoogle.asStateFlow()

    fun selectItem(item: LibraryItem) {
        _selectedItem.value = item
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

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _state.value = interactor.loadInitialData()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val currentData = (_state.value as? State.Success)?.data ?: return@launch
            _state.value = interactor.loadNextPage(currentData)
        }
    }
    fun loadPreviousPage() {
        viewModelScope.launch {
            val currentData = (_state.value as? State.Success)?.data ?: return@launch
            _state.value = interactor.loadPreviousPage(currentData)
        }
    }

    fun searchBooks(author: String, title: String) {
        viewModelScope.launch {
            _stateGoogle.value = interactor.searchBooks(author, title)
        }
    }


    fun addItem(item: LibraryItem) {
        viewModelScope.launch {
            _state.value = interactor.addItem(item)
            loadInitialData()
        }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch {
            _state.value = interactor.updateSortOrder(order)
            loadInitialData()
        }
    }
}