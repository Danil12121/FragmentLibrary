package com.example.libraryui.data.repository

import android.content.SharedPreferences
import com.example.libraryui.data.api.GetGoogleBooks
import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.data.dao.BookDao
import com.example.libraryui.data.dao.DiskDao
import com.example.libraryui.data.dao.NewspaperDao
import com.example.libraryui.data.entity.BaseEntity
import com.example.libraryui.domain.repository.LibraryRepository
import com.example.libraryui.domain.models.Book
import com.example.libraryui.domain.models.Disk
import com.example.libraryui.domain.models.Newspaper
import com.example.libraryui.common.extensions.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.max

class LibraryRepositoryImpl(
    private val bookDao: BookDao,
    private val diskDao: DiskDao,
    private val newspaperDao: NewspaperDao,
    val prefs: SharedPreferences
) : LibraryRepository {


    private val _isLoading = MutableStateFlow(false)
    private var currentOffset = 0
    private var totalItems = 0

    override suspend fun loadInitialData(): List<LibraryItem> {
        if (_isLoading.value) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val startTime = System.currentTimeMillis()
                val sortOrder = prefs.getString(LibraryRepository.Companion.SORT_KEY, "title")

                val entities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(
                        offset = 0, limit = LibraryRepository.Companion.PAGE_SIZE
                    )

                    "date" -> loadItemsSortedByDate(
                        offset = 0, limit = LibraryRepository.Companion.PAGE_SIZE
                    )

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

    override suspend fun loadNextPage(): List<LibraryItem> {
        if (_isLoading.value || currentOffset >= totalItems) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val sortOrder =
                    prefs.getString(LibraryRepository.Companion.SORT_KEY, "title") ?: "title"

                val newEntities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(
                        currentOffset, LibraryRepository.Companion.PAGE_SIZE / 2
                    )

                    "date" -> loadItemsSortedByDate(
                        currentOffset, LibraryRepository.Companion.PAGE_SIZE / 2
                    )

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

    override suspend fun loadPreviousPage(): List<LibraryItem> {
        if (_isLoading.value || currentOffset <= 0) return emptyList()

        return withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val sortOrder =
                    prefs.getString(LibraryRepository.Companion.SORT_KEY, "title") ?: "title"
                val prevOffset = max(0, currentOffset - LibraryRepository.Companion.PAGE_SIZE / 2)

                val previousEntities = when (sortOrder) {
                    "title" -> loadItemsSortedByTitle(
                        prevOffset, LibraryRepository.Companion.PAGE_SIZE / 2
                    )

                    "date" -> loadItemsSortedByDate(
                        prevOffset, LibraryRepository.Companion.PAGE_SIZE / 2
                    )

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

    override suspend fun addItem(item: LibraryItem) {
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

        return (books + disks + newspapers).sortedBy { it.title }.take(limit)
    }

    private suspend fun loadItemsSortedByDate(offset: Int, limit: Int): List<BaseEntity> {
        val books = bookDao.getPagedByDate(offset, limit)
        val disks = diskDao.getPagedByDate(offset, limit)
        val newspapers = newspaperDao.getPagedByDate(offset, limit)

        return (books + disks + newspapers).sortedBy { it.createdAt }.take(limit)
    }

    companion object {
        const val Google_Book_URL = "https://www.googleapis.com/books/v1/"
        const val MAX_RESULTS = 20
    }

    private val apiService = Retrofit.Builder().baseUrl(Google_Book_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GetGoogleBooks::class.java)


    override suspend fun searchBooks(author: String, title: String): List<Book> {
        val query = buildString {
            if (author.isNotEmpty()) append("inauthor:$author ")
            if (title.isNotEmpty()) append("intitle:$title")
        }.trim()

        val response = apiService.searchBooks(
            query = query,
            maxResults = MAX_RESULTS,
            fields = "items(id,volumeInfo(title,authors,pageCount))"
        )

        return response.items?.mapNotNull { it.toBook() } ?: emptyList()
    }
}