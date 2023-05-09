package com.volokhinaleksey.interactors.main

import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.ui.TrackUI

/**
 * A main object for the business logic of the application without dependence on the platform
 */

interface MainInteractor {

    /**
     * Method for getting a list of songs
     * @param query - Request for filtering by which to get a list of songs
     */

    suspend fun getSongs(
        query: Array<String>
    ): TrackState

    /**
     * Method for adding/deleting a favorite song
     * @param trackUI - The song to delete
     */

    suspend fun upsertFavoriteSong(trackUI: TrackUI)

}