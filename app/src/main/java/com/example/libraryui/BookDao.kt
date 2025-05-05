package com.example.libraryui

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookDao: BaseDao<BookEntity> {
    @Query("SELECT * FROM books ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedBooks(offset: Int, limit: Int): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getById(id: Int): BookEntity?

    @Query("SELECT * FROM books ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedByTitle(offset: Int, limit: Int): List<BookEntity>

    @Query("SELECT * FROM books ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedByDate(offset: Int, limit: Int): List<BookEntity>

    @Query("SELECT COUNT(*) FROM books")
    suspend fun getCount(): Int
}