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
import java.io.Serializable

class MainViewModel(
    private val repository: LibraryRepository
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

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val items = repository.loadInitialData()
                _state.value = State.Success(items)
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val currentState = _state.value as? State.Success ?: return@launch

            try {
                val newItems = repository.loadNextPage()
                if (newItems.isNotEmpty()) {
                    _state.value = State.Success(currentState.data + newItems)
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки следующей страницы: ${e.message}")
            }
        }
    }
    fun loadPreviousPage() {
        viewModelScope.launch {
            val currentState = _state.value as? State.Success ?: return@launch

            try {
                val previousItems = repository.loadPreviousPage()
                if (previousItems.isNotEmpty()) {
                    _state.value = State.Success(previousItems + currentState.data)
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки предыдущей страницы: ${e.message}")
            }
        }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch {
            repository.prefs.edit().putString(LibraryRepository.SORT_KEY, order).apply()
            loadInitialData()
        }
    }


    fun addItem(item: LibraryItem) {
        viewModelScope.launch {
            try {
                repository.addItem(item)
                loadInitialData()
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка добавления: ${e.message}")
            }
        }
    }
}
