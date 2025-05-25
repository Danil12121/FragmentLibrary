package com.example.libraryui.common.utils

import com.example.libraryui.domain.models.LibraryItem

sealed class State {
    object Loading : State()
    data class Success(val data: List<LibraryItem>) : State()
    data class Error(val message: String) : State()
}