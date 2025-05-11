package com.example.libraryui.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.libraryui.data.dao.BookDao
import com.example.libraryui.data.dao.DiskDao
import com.example.libraryui.data.dao.NewspaperDao
import com.example.libraryui.data.entity.BookEntity
import com.example.libraryui.data.entity.DiskEntity
import com.example.libraryui.data.entity.NewspaperEntity

@Database(
    entities = [BookEntity::class, DiskEntity::class, NewspaperEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LibDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun diskDao(): DiskDao
    abstract fun newspaperDao(): NewspaperDao
}