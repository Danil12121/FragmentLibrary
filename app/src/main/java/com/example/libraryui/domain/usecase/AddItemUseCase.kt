package com.example.libraryui.domain.usecase

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.common.utils.State

class AddItemUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(item: LibraryItem): State {
        return try {
            repository.addItem(item)
            State.Success(emptyList())
        } catch (e: Exception) {
            State.Error("Ошибка добавления: ${e.message}")
        }
    }
}