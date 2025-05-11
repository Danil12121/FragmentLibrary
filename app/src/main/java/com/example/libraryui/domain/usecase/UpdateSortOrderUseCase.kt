package com.example.libraryui.domain.usecase
import android.provider.Settings.System.putString
import androidx.core.content.edit
import com.example.libraryui.common.utils.State
import com.example.libraryui.data.repository.LibraryRepositoryImpl
import com.example.libraryui.domain.repository.LibraryRepository


class UpdateSortOrderUseCase(
    private val repository: LibraryRepositoryImpl
) {
    suspend operator fun invoke(order: String): State {
        return try {
            repository.prefs.edit { putString(LibraryRepository.SORT_KEY, order) }
            State.Success(emptyList())
        } catch (e: Exception) {
            State.Error("Ошибка сортировки: ${e.message}")
        }
    }
}