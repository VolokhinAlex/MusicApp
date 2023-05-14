package com.volokhinaleksey.interactors.description

import com.volokhinaleksey.interactors.main.MainInteractor
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow

/**
 * A business logic object for description screen without platform dependency
 */

interface DescriptionInteractor : MainInteractor {

    /**
     * Method for getting a favorite song by its name
     * @param title - The name of the song to get it from the list of favorite songs
     */

    fun getFavoriteSongByTitle(title: String): Flow<TrackUI>

}