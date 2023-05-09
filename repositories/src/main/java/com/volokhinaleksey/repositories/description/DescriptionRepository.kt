package com.volokhinaleksey.repositories.description

import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow

/**
 * Repository object for the song details screen
 */

interface DescriptionRepository {

    /**
     * Method for getting a favorite song by its name
     * @param title - The name of the song, to get it from the favorites list
     */

    fun getFavoriteSongByTitle(title: String): Flow<TrackUI>
}