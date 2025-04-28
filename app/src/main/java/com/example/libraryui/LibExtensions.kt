package com.example.libraryui

fun Disk.toEntity(): DiskEntity = DiskEntity(
    id = id.toInt(),
    title = title,
    diskType = diskType,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)

fun Newspaper.toEntity(): NewspaperEntity = NewspaperEntity(
    id = id.toInt(),
    title = title,
    releaseNumber = releaseNumber,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id.toInt(),
    title = title,
    author = author,
    pageCount = pageCount,
    createdAt = createdAt,
    imageId = imageID,
    isEnable = isEnable,
    name = name
)