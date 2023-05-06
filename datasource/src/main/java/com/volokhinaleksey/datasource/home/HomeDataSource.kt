package com.volokhinaleksey.datasource.home

import com.volokhinaleksey.models.local.FavoriteEntity
import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.local.SongEntity
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {

    suspend fun getSongs(): List<LocalTrack>

    fun getFavoriteSongs(): Flow<List<LocalTrack>>

    suspend fun insertFavoriteSong(favoriteEntity: FavoriteEntity)

    suspend fun insertSong(songEntity: SongEntity)

    suspend fun upsertFavoriteSong(favoriteEntity: FavoriteEntity)
    fun getFavoriteSongByTitle(title: String): Flow<LocalTrack>
}