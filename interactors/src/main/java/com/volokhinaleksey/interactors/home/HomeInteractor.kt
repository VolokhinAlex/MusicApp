package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.interactors.main.MainInteractor
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow

/**
 * A business logic object for home screen without platform dependency
 */

interface HomeInteractor : MainInteractor {

    /**
     * Method for getting a list of favorite songs
     */

    fun getFavoriteSongs(): Flow<List<TrackUI>>

}