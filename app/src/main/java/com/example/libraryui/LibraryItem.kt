package com.example.libraryui

import java.io.Serializable

abstract class LibraryItem(
    val imageID: Int,
    val id: Int,
    var isEnable: Boolean,
    val name: String
) : Serializable
