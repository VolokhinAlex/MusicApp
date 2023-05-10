package com.volokhinaleksey.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class SongEntity(
    val id: Long,
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val path: String,
    @ColumnInfo("album_id")
    val albumId: Long,
    @ColumnInfo("album_title")
    val albumTitle: String,
    val artist: String,
    val duration: Long
)
