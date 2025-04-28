package com.example.libraryui

import androidx.room.Database
import androidx.room.RoomDatabase

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