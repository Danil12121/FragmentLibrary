package com.example.libraryui.data.entity

import com.example.libraryui.domain.models.LibraryItem

interface BaseEntity {
    fun toDomain() : LibraryItem
    val title : String
    val createdAt: Long
}