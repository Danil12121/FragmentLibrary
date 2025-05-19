package com.example.libraryui.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.libraryui.data.entity.DiskEntity

@Dao
interface DiskDao: BaseDao<DiskEntity> {
    @Query("SELECT * FROM disks ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedBooks(offset: Int, limit: Int): List<DiskEntity>

    @Query("SELECT * FROM disks WHERE id = :id")
    suspend fun getById(id: Int): DiskEntity?

    @Query("SELECT * FROM disks ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun getPagedByTitle(offset: Int, limit: Int): List<DiskEntity>

    @Query("SELECT * FROM disks ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedByDate(offset: Int, limit: Int): List<DiskEntity>

    @Query("SELECT COUNT(*) FROM disks")
    suspend fun getCount(): Int
}