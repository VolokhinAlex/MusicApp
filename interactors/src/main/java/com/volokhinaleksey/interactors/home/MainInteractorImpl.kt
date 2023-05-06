package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.core.utils.mapLocalTrackToTrackUI
import com.volokhinaleksey.core.utils.mapTrackUIToLocalTrack
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.TrackState.Success
import com.volokhinaleksey.models.ui.TrackUI
import com.volokhinaleksey.repositories.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainInteractorImpl(
    private val repository: MainRepository
) : MainInteractor {

    override suspend fun getSongs(): TrackState {
        return Success(tracks = repository.getSongs().map {
            mapLocalTrackToTrackUI(it)
        })
    }

    override fun getFavoriteSongs(): Flow<List<TrackUI>> {
        return repository.getFavoriteSongs().map { localTracks ->
            localTracks.map { localTrack ->
                mapLocalTrackToTrackUI(localTrack = localTrack)
            }
        }
    }

    override suspend fun upsertFavoriteSong(trackUI: TrackUI) {
        repository.upsertFavoriteSong(localTrack = mapTrackUIToLocalTrack(trackUI))
    }

    override fun getFavoriteSongByTitle(title: String): Flow<TrackUI> {
        return repository.getFavoriteSongByTitle(title = title)
    }
}