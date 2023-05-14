package com.volokhinaleksey.repositories.home

import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow

/**
 * Repository object for the song main screen
 */

interface HomeRepository {

    /**
     * Method for getting a list of favorite songs
     */

    fun getFavoriteSongs(): Flow<List<LocalTrack>>

}