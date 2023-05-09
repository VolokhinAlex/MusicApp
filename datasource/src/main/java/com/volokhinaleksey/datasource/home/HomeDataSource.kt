package com.volokhinaleksey.datasource.home

import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow

/**
 * The data source object for the home screen
 */

interface HomeDataSource {

    /**
     * Method for getting a list of favorite songs
     */

    fun getFavoriteSongs(): Flow<List<LocalTrack>>

}