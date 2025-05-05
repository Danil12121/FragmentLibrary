package com.example.libraryui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.isNotEmpty
import kotlin.math.max


class LibraryRepository(
    private val bookDao: BookDao,
    private val diskDao: DiskDao,
    private val newspaperDao: NewspaperDao,
    val prefs: SharedPreferences
) {
    companion object {
        private const val PAGE_SIZE = 18
        private const val N2 = PAGE_SIZE - 10
        const val SORT_KEY = "sort_order"
    }

    private val _isLoading = MutableStateFlow(false)
    private var currentOffset = 0
    private var totalItems = 0

    suspend fun loadInitialData(): List<LibraryItem> {
        if (_isLoading.value) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val startTime = System.currentTimeMillis()
                val sortOrder = prefs.getString(SORT_KEY, "title")

                val entities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(offset = 0, limit = PAGE_SIZE)
                    "date" -> loadItemsSortedByDate(offset = 0, limit = PAGE_SIZE)
                    else -> emptyList()
                }

                val loadTime = System.currentTimeMillis() - startTime
                if (loadTime < 1000) delay(1000 - loadTime)

                entities.map { it.toDomain() }
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun loadNextPage(): List<LibraryItem> {
        if (_isLoading.value || currentOffset >= totalItems) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val sortOrder = prefs.getString(SORT_KEY, "title") ?: "title"

                val newEntities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(currentOffset, PAGE_SIZE / 2)
                    "date" -> loadItemsSortedByDate(currentOffset, PAGE_SIZE / 2)
                    else -> emptyList()
                }

                if (newEntities.isNotEmpty()) {
                    currentOffset += newEntities.size
                    newEntities.map { it.toDomain() }
                } else {
                    emptyList()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun loadPreviousPage(): List<LibraryItem> {
        if (_isLoading.value || currentOffset <= 0) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val sortOrder = prefs.getString(SORT_KEY, "title") ?: "title"
                val prevOffset = max(0, currentOffset - PAGE_SIZE / 2)

                val previousEntities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(prevOffset, PAGE_SIZE / 2)
                    "date" -> loadItemsSortedByDate(prevOffset, PAGE_SIZE / 2)
                    else -> emptyList()
                }

                if (previousEntities.isNotEmpty()) {
                    currentOffset = prevOffset
                    previousEntities.map { it.toDomain() }
                } else {
                    emptyList()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun addItem(item: LibraryItem) {
        withContext(Dispatchers.IO) {
            when (item) {
                is Book -> bookDao.insert(item.toEntity())
                is Disk -> diskDao.insert(item.toEntity())
                is Newspaper -> newspaperDao.insert(item.toEntity())
            }
            loadInitialData()
        }
    }

    private suspend fun loadItemsSortedByTitle(offset: Int, limit: Int): List<BaseEntity> {
        val books = bookDao.getPagedByTitle(offset, limit)
        val disks = diskDao.getPagedByTitle(offset, limit)
        val newspapers = newspaperDao.getPagedByTitle(offset, limit)

        return (books + disks + newspapers)
            .sortedBy { it.title }
            .take(limit)
    }

    private suspend fun loadItemsSortedByDate(offset: Int, limit: Int): List<BaseEntity> {
        val books = bookDao.getPagedByDate(offset, limit)
        val disks = diskDao.getPagedByDate(offset, limit)
        val newspapers = newspaperDao.getPagedByDate(offset, limit)

        return (books + disks + newspapers)
            .sortedBy { it.createdAt }
            .take(limit)
    }

    private val apiService = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GetGoogleBooks::class.java)


    suspend fun searchBooks(author: String, title: String): List<Book> {
        val query = buildString {
            if (author.isNotEmpty()) append("inauthor:$author ")
            if (title.isNotEmpty()) append("intitle:$title")
        }.trim()

        val response = apiService.searchBooks(
            query = query,
            maxResults = 20,
            fields = "items(id,volumeInfo(title,authors,pageCount))"
        )

        return response.items?.mapNotNull { it.toBook() } ?: emptyList()
    }
}