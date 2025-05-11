package com.example.libraryui.common.extensions

import com.example.libraryui.data.entity.BookEntity
import com.example.libraryui.data.entity.DiskEntity
import com.example.libraryui.data.entity.NewspaperEntity
import com.example.libraryui.domain.models.Book
import com.example.libraryui.domain.models.Disk
import com.example.libraryui.domain.models.Newspaper

fun Disk.toEntity(): DiskEntity = DiskEntity(
    title = title,
    diskType = diskType,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)

fun Newspaper.toEntity(): NewspaperEntity = NewspaperEntity(
    title = title,
    releaseNumber = releaseNumber,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)

fun Book.toEntity(): BookEntity = BookEntity(
    title = title,
    author = author,
    pageCount = pageCount,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)