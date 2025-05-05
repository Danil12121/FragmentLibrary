package com.example.libraryui

data class GoogleBook(
    val id: String,
    val volumeInfo: VolumeInfo
) {
    fun toBook(): Book? {
        return Book(
            id = id.hashCode(),
            title = volumeInfo.title ?: return null,
            author = volumeInfo.authors?.joinToString(", ") ?: "",
            pageCount = volumeInfo.pageCount ?: 0,
            createdAt = System.currentTimeMillis(),
        )
    }
}
