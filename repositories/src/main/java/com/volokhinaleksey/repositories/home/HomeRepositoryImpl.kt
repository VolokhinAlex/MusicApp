package com.volokhinaleksey.repositories.home

import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the repository object for the home screen
 * @param homeDataSource - The data source from which data can be obtained
 */

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource
) : HomeRepository {

    /**
     * Method for getting a list of favorite songs
     */

    override fun getFavoriteSongs(): Flow<List<LocalTrack>> {
        return homeDataSource.getFavoriteSongs()
    }

}