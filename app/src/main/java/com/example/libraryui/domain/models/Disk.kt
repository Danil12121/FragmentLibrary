package com.example.libraryui.domain.models

import com.example.libraryui.domain.models.LibraryItem
import com.example.libraryui.R


class Disk(
    override val id: Int,
    title: String,
    val diskType: String,
    createdAt: Long

) : LibraryItem(imageID = R.drawable.disk, id = id, isEnable = true, name = "Disk", createdAt = System.currentTimeMillis(), title=title) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as Disk
        if (id != other.id) return false
        if (title != other.title) return false
        if (diskType != other.diskType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + diskType.hashCode()
        return result
    }
}