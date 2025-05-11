package com.example.libraryui.domain.usecase
import com.example.libraryui.common.utils.State
import com.example.libraryui.domain.repository.LibraryRepository

class SearchBooksUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(author: String, title: String): State {
        return try {
            val books = repository.searchBooks(author, title)
            State.Success(books)
        } catch (e: Exception) {
            State.Error("Ошибка поиска: ${e.message}")
        }
    }
}