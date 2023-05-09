package com.volokhinaleksey.repositories.main

import com.volokhinaleksey.core.utils.mapLocalTrackToFavoriteEntity
import com.volokhinaleksey.core.utils.mapTrackLocalToSongEntity
import com.volokhinaleksey.datasource.main.MainDataSource
import com.volokhinaleksey.models.local.LocalTrack

/**
 * Implementation of the main repository object with basic methods for all screens
 * @param mainDataSource - The data source from which data can be obtained
 */

class MainRepositoryImpl(
    private val mainDataSource: MainDataSource
) : MainRepository {

    /**
     * Method for getting a list of songs
     * @param query - An additional condition for getting a list of songs
     */

    override suspend fun getSongs(query: Array<String>): List<LocalTrack> {
        return mainDataSource.getSongs(query = query).map {
            mainDataSource.insertSong(mapTrackLocalToSongEntity(localTrack = it))
            mainDataSource.insertFavoriteSong(mapLocalTrackToFavoriteEntity(localTrack = it))
            it
        }
    }

    /**
     * Method for adding or deleting a song to favorites
     * @param localTrack - Track to add to favorites
     */

    override suspend fun upsertFavoriteSong(localTrack: LocalTrack) {
        mainDataSource.upsertFavoriteSong(mapLocalTrackToFavoriteEntity(localTrack = localTrack))
    }

}