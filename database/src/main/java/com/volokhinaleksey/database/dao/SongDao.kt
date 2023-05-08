package com.volokhinaleksey.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.volokhinaleksey.models.local.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM song where title LIKE :title")
    suspend fun getSongByTitle(title: String): SongEntity

    @Query("SELECT * FROM song")
    suspend fun all(): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SongEntity)

    @Update
    suspend fun update(entity: SongEntity)

    @Delete
    suspend fun delete(entity: SongEntity)

}