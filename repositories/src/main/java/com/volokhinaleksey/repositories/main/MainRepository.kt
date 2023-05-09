package com.volokhinaleksey.repositories.main

import com.volokhinaleksey.models.local.LocalTrack

/**
 * The main repository object for getting data from some remote source
 */

interface MainRepository {

    /**
     * Method for getting a list of songs
     * @param query - An additional condition for getting a list of songs
     */

    suspend fun getSongs(query: Array<String>): List<LocalTrack>

    /**
     * Method for adding or deleting a song to favorites
     * @param localTrack - Track to add to favorites
     */

    suspend fun upsertFavoriteSong(localTrack: LocalTrack)
}