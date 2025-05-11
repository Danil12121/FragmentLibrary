package com.example.libraryui.domain.usecase
import android.util.Log
import com.example.libraryui.common.utils.State
import com.example.libraryui.data.repository.LibraryRepositoryImpl
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.domain.repository.LibraryRepository

class Interactor(
    repository: LibraryRepositoryImpl
) {
    private val loadInitialDataUseCase = LoadInitialDataUseCase(repository)
    private val loadNextPageUseCase = LoadNextPageUseCase(repository)
    private val loadPreviousPageUseCase = LoadPreviousPageUseCase(repository)
    private val searchBooksUseCase = SearchBooksUseCase(repository)
    private val addItemUseCase = AddItemUseCase(repository)
    private val updateSortOrderUseCase = UpdateSortOrderUseCase(repository)
    suspend fun loadInitialData(): State {
        return try {
            loadInitialDataUseCase()
        } catch (e: Exception) {
            State.Error("Ошибка загрузки данных: ${e.message}")
        }
    }

    suspend fun loadNextPage(currentItems: List<LibraryItem>): State {
        return try {
            loadNextPageUseCase(currentItems)
        } catch (e: Exception) {
            State.Error("Ошибка загрузки следующей страницы: ${e.message}")
        }
    }

    suspend fun loadPreviousPage(currentItems: List<LibraryItem>): State {
        return try {
            loadPreviousPageUseCase(currentItems)
        } catch (e: Exception) {
            State.Error("Ошибка загрузки предыдущей страницы: ${e.message}")
        }
    }

    suspend fun searchBooks(author: String, title: String): State {
        return try {
            searchBooksUseCase(author, title)
        } catch (e: Exception) {
            Log.d("AAA", "Ошибка поиска: ${e.message}")
            State.Error("Ошибка поиска: ${e.message}")

        }
    }

    suspend fun addItem(item: LibraryItem): State {
        return try {
            addItemUseCase(item)
            State.Success(emptyList())
        } catch (e: Exception) {
            State.Error("Ошибка добавления: ${e.message}")
        }
    }

    suspend fun updateSortOrder(order: String): State {
        return try {
            updateSortOrderUseCase(order)
            State.Success(emptyList())
        } catch (e: Exception) {
            State.Error("Ошибка изменения сортировки: ${e.message}")
        }
    }

}