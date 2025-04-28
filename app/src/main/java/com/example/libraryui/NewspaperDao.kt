package com.example.libraryui

import androidx.room.Dao
import androidx.room.Query

@Dao
interface NewspaperDao: BaseDao<NewspaperEntity> {
    @Query("SELECT * FROM newspapers ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedBooks(offset: Int, limit: Int): List<NewspaperEntity>

    @Query("SELECT * FROM newspapers WHERE id = :id")
    suspend fun getById(id: Int): NewspaperEntity?

    @Query("SELECT * FROM newspapers ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedByTitle(offset: Int, limit: Int): List<NewspaperEntity>

    @Query("SELECT * FROM newspapers ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedByDate(offset: Int, limit: Int): List<NewspaperEntity>

    @Query("SELECT COUNT(*) FROM newspapers")
    suspend fun getCount(): Int
}