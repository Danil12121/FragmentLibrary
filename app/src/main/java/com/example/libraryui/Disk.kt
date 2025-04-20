package com.example.libraryui

class Disk(
    id: Int, val title: String, val diskType: String

) : LibraryItem(imageID = R.drawable.disk, id = id, isEnable = true, name = "Disk") {
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