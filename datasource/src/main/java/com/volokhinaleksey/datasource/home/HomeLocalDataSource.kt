package com.volokhinaleksey.datasource.home

import com.volokhinaleksey.core.utils.mapSongEntityToLocalTrack
import com.volokhinaleksey.database.MusicDatabase
import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementing a local data source for the home screen
 */

class HomeLocalDataSource(
    private val db: MusicDatabase
) : HomeDataSource {

    /**
     * Method for getting a list of favorite songs
     */

    override fun getFavoriteSongs(): Flow<List<LocalTrack>> {
        return db.favoriteDao().all().map {
            it.map { favorite ->
                val song = db.songDao().getSongByTitle(title = favorite.title)
                mapSongEntityToLocalTrack(
                    songEntity = song,
                    favoriteEntity = favorite
                )
            }
        }
    }

}