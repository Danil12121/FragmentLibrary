package com.example.libraryui

sealed class State {
    object Loading : State()
    data class Success(val data: List<LibraryItem>) : State()
    data class Error(val message: String) : State()
}