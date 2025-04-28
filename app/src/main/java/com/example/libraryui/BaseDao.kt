package com.example.libraryui

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

@Dao
interface BaseDao<T : BaseEntity> {
    @Insert(onConflict = REPLACE)
    suspend fun insert(item: T): Long

    @Update(onConflict = REPLACE)
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)
}