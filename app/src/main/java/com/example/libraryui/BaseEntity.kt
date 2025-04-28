package com.example.libraryui

interface BaseEntity {
    fun toDomain() : LibraryItem
    val title : String
    val createdAt: Long
}