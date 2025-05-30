package com.example.libraryui.domain.models

import java.io.Serializable

abstract class LibraryItem(
    val imageID: Int,
    open val id: Int,
    var isEnable: Boolean,
    val name: String,
    val createdAt: Long,
    val title: String
) : Serializable