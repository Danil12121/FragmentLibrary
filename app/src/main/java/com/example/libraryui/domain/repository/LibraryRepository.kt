package com.example.libraryui.domain.repository

import com.example.libraryui.domain.models.Book
import com.example.libraryui.domain.models.LibraryItem

interface LibraryRepository {
    suspend fun loadInitialData(): List<LibraryItem>
    suspend fun loadNextPage(): List<LibraryItem>
    suspend fun loadPreviousPage(): List<LibraryItem>
    suspend fun addItem(item: LibraryItem)
    suspend fun searchBooks(author: String, title: String): List<Book>

    companion object {
        const val PAGE_SIZE = 18
        const val N2 = PAGE_SIZE - 10
        const val SORT_KEY = "sort_order"
    }
}