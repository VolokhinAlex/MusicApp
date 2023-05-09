package com.volokhinaleksey.interactors.main

import com.volokhinaleksey.core.utils.mapLocalTrackToTrackUI
import com.volokhinaleksey.core.utils.mapTrackUIToLocalTrack
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.TrackState.Success
import com.volokhinaleksey.models.ui.TrackUI
import com.volokhinaleksey.repositories.main.MainRepository

/**
 * Implementation of the main object for the business logic of the application without dependence on the platform
 */

class MainInteractorImpl(
    private val repository: MainRepository
) : MainInteractor {

    /**
     * Method for getting a list of songs
     * @param query - Request for filtering by which to get a list of songs
     */

    override suspend fun getSongs(query: Array<String>): TrackState {
        return Success(tracks = repository.getSongs(query = query).map {
            mapLocalTrackToTrackUI(it)
        })
    }

    /**
     * Method for adding/deleting a favorite song
     * @param trackUI - The song to delete
     */

    override suspend fun upsertFavoriteSong(trackUI: TrackUI) {
        repository.upsertFavoriteSong(localTrack = mapTrackUIToLocalTrack(trackUI))
    }

}