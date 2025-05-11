package com.example.libraryui.domain.models

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.R


class Book(
    override val id: Int,
    title: String,
    val author: String,
    val pageCount: Int,
    createdAt: Long,

    ) : LibraryItem(imageID = R.drawable.book, id = id, isEnable = true, name = "Book", createdAt = System.currentTimeMillis(), title=title) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as Book
        if (id != other.id) return false
        if (title != other.title) return false
        if (author != other.author) return false
        if (pageCount != other.pageCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pageCount
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }
}