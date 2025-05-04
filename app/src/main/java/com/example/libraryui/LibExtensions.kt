package com.example.libraryui

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