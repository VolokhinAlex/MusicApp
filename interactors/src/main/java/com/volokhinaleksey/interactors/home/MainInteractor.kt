package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow

interface MainInteractor {
    suspend fun getSongs(): TrackState

    fun getFavoriteSongs(): Flow<List<TrackUI>>

    suspend fun upsertFavoriteSong(trackUI: TrackUI)

    fun getFavoriteSongByTitle(title: String): Flow<TrackUI>
}