package com.example.libraryui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.libraryui.domain.models.Newspaper
import com.example.libraryui.R

@Entity(tableName = "newspapers")
data class NewspaperEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    override val title: String,
    val releaseNumber: Int,
    override val createdAt: Long = System.currentTimeMillis(),
    val imageId: Int = R.drawable.newspaper,
    var isEnable: Boolean = true,
    val name: String = "Newspaper"
) : BaseEntity {
    override fun toDomain(): Newspaper = Newspaper(
        id = id.toInt(),
        title = title,
        releaseNumber = releaseNumber,
        createdAt = createdAt,
    )
}