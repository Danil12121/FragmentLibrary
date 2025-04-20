package com.example.libraryui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun setCurrentItem(item: LibraryItem){
        currentItem.value = item
    }
}