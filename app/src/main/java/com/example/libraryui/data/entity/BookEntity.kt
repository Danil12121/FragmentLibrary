package com.example.libraryui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.libraryui.domain.models.Book
import com.example.libraryui.R

@Entity(tableName = "books")
class BookEntity (
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    override val title: String,
    val author: String,
    val pageCount: Int,
    override val createdAt: Long,
    val imageId: Int = R.drawable.book,
    var isEnable: Boolean = true,
    val name: String = "Book"
) : BaseEntity {
    override fun toDomain(): Book = Book(
        id = id.toInt(),
        title = title,
        author = author,
        pageCount = pageCount,
        createdAt = createdAt,
    )
}