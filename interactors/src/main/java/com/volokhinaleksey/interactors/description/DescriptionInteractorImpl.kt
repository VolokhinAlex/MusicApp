package com.volokhinaleksey.interactors.description

import com.volokhinaleksey.interactors.main.MainInteractor
import com.volokhinaleksey.models.ui.TrackUI
import com.volokhinaleksey.repositories.description.DescriptionRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of a business logic object for the description screen without dependence on the platform
 */

class DescriptionInteractorImpl(
    private val repository: DescriptionRepository,
    private val mainInteractor: MainInteractor
) : DescriptionInteractor, MainInteractor by mainInteractor {

    /**
     * Method for getting a favorite song by its name
     * @param title - The name of the song to get it from the list of favorite songs
     */

    override fun getFavoriteSongByTitle(title: String): Flow<TrackUI> {
        return repository.getFavoriteSongByTitle(title = title)
    }

}