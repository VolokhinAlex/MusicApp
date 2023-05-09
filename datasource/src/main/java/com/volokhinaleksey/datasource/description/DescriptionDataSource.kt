package com.volokhinaleksey.datasource.description

import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow

/**
 * The data source object for the description screen
 */

interface DescriptionDataSource {

    /**
     *  Method for getting a favorite song by its name
     *  @param title - The name of the song by which you need to find it in the list of favorite songs
     */

    fun getFavoriteSongByTitle(title: String): Flow<LocalTrack>

}