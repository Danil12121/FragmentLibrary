package com.example.libraryui.domain.usecase

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.common.utils.State

class LoadNextPageUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(currentItems: List<LibraryItem>): State {
        return try {
            val newItems = repository.loadNextPage()
            if (newItems.isNotEmpty()) {
                State.Success(currentItems + newItems)
            } else {
                State.Success(currentItems)
            }
        } catch (e: Exception) {
            State.Error("Ошибка загрузки следующей страницы: ${e.message}")
        }
    }
}