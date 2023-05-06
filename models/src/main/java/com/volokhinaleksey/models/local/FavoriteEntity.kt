package com.volokhinaleksey.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_song", foreignKeys = [ForeignKey(
        entity = SongEntity::class,
        parentColumns = ["title"],
        childColumns = ["title"]
    )]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    val title: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean
)