package com.example.libraryui

class Newspaper(
    id: Int, val title: String, val releaseNumber: Int

) : LibraryItem(imageID = R.drawable.newspaper, id = id, isEnable = true, name = "Newspaper") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as Newspaper
        if (id != other.id) return false
        if (title != other.title) return false
        if (releaseNumber != other.releaseNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = releaseNumber
        result = 31 * result + title.hashCode()
        return result
    }
}