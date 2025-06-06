package com.example.libraryui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.libraryui.domain.models.Disk
import com.example.libraryui.R

@Entity(tableName = "disks")
data class DiskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    override val title: String,
    val diskType: String,
    override val createdAt: Long = System.currentTimeMillis(),
    val imageId: Int = R.drawable.disk,
    var isEnable: Boolean = true,
    val name: String = "Disk"
) : BaseEntity {
    override fun toDomain(): Disk = Disk(
        id = id.toInt(),
        title = title,
        diskType = diskType,
        createdAt = createdAt,
    )
}