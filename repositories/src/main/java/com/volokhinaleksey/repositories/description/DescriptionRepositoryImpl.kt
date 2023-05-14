package com.volokhinaleksey.repositories.description

import com.volokhinaleksey.core.utils.mapLocalTrackToTrackUI
import com.volokhinaleksey.datasource.description.DescriptionDataSource
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of the repository object for the details screen
 * @param descriptionDataSource - The data source from which data can be obtained
 */

class DescriptionRepositoryImpl(
    private val descriptionDataSource: DescriptionDataSource
) : DescriptionRepository {

    /**
     * Method for getting a favorite song by its name
     * @param title - The name of the song, to get it from the favorites list
     */

    override fun getFavoriteSongByTitle(title: String): Flow<TrackUI> {
        return descriptionDataSource.getFavoriteSongByTitle(title = title).map {
            mapLocalTrackToTrackUI(localTrack = it)
        }
    }

}