package com.example.libraryui.domain.usecase

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.common.utils.State

class LoadPreviousPageUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(currentItems: List<LibraryItem>): State {
        return try {
            val previousItems = repository.loadPreviousPage()
            if (previousItems.isNotEmpty()) {
                State.Success(previousItems + currentItems)
            } else {
                State.Success(currentItems)
            }
        } catch (e: Exception) {
            State.Error("Ошибка загрузки предыдущей страницы: ${e.message}")
        }
    }
}