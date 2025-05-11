package com.example.libraryui.domain.models

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.R


class Newspaper(
    override val id: Int,
    title: String,
    val releaseNumber: Int,
    createdAt: Long

) : LibraryItem(imageID = R.drawable.newspaper, id = id, isEnable = true, name = "Newspaper", createdAt = System.currentTimeMillis(), title=title) {
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