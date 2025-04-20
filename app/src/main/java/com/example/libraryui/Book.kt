package com.example.libraryui

class Book(
    id: Int,
    val title: String,
    val author: String,
    val pageCount: Int,

    ) : LibraryItem(imageID = R.drawable.book, id = id, isEnable = true, name = "Book") {
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