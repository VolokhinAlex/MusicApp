package com.volokhinaleksey.repositories

import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getSongs(): List<LocalTrack>

    fun getFavoriteSongs(): Flow<List<LocalTrack>>

    suspend fun upsertFavoriteSong(localTrack: LocalTrack)
    fun getFavoriteSongByTitle(title: String): Flow<TrackUI>
}