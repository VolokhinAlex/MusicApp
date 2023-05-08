package com.volokhinaleksey.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.volokhinaleksey.database.dao.FavoriteDao
import com.volokhinaleksey.database.dao.SongDao
import com.volokhinaleksey.models.local.FavoriteEntity
import com.volokhinaleksey.models.local.SongEntity

@Database(
    entities = [FavoriteEntity::class, SongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun songDao(): SongDao

}