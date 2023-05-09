package com.volokhinaleksey.datasource.main

import com.volokhinaleksey.models.local.FavoriteEntity
import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.local.SongEntity

/**
 * The object of the main data source for all screens
 */

interface MainDataSource {

    /**
     * Method for getting a list of songs from a data source
     * @param query - An additional condition for obtaining data on it
     */

    suspend fun getSongs(query: Array<String>): List<LocalTrack>

    /**
     * Method for inserting a favorite song into a database table
     * @param favoriteEntity - The object of the favorite song
     */

    suspend fun insertFavoriteSong(favoriteEntity: FavoriteEntity)

    /**
     * Method for adding a song to a database table
     * @param songEntity - The song to add to the database
     */

    suspend fun insertSong(songEntity: SongEntity)

    /**
     * Method for adding/updating data of a favorite song
     * @param favoriteEntity - The object of the favorite song
     */

    suspend fun upsertFavoriteSong(favoriteEntity: FavoriteEntity)
}