package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.core.utils.mapLocalTrackToTrackUI
import com.volokhinaleksey.interactors.main.MainInteractor
import com.volokhinaleksey.models.ui.TrackUI
import com.volokhinaleksey.repositories.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of a business logic object for the home screen without dependence on the platform
 */

class HomeInteractorImpl(
    private val repository: HomeRepository,
    private val mainInteractor: MainInteractor
) : HomeInteractor, MainInteractor by mainInteractor {

    /**
     * Method for getting a list of favorite songs
     */

    override fun getFavoriteSongs(): Flow<List<TrackUI>> {
        return repository.getFavoriteSongs().map { localTracks ->
            localTracks.map { localTrack ->
                mapLocalTrackToTrackUI(localTrack = localTrack)
            }
        }
    }

}