package com.example.libraryui.domain.usecase

import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.common.utils.State

class LoadInitialDataUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): State {
        return try {
            val items = repository.loadInitialData()
            State.Success(items)
        } catch (e: Exception) {
            State.Error("Ошибка загрузки: ${e.message}")
        }
    }
}