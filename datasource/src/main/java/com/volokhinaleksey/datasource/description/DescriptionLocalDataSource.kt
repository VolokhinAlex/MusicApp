package com.volokhinaleksey.datasource.description

import com.volokhinaleksey.core.utils.mapSongEntityToLocalTrack
import com.volokhinaleksey.database.MusicDatabase
import com.volokhinaleksey.models.local.LocalTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementing a local data source for the description screen
 */

class DescriptionLocalDataSource(private val db: MusicDatabase) : DescriptionDataSource {

    /**
     *  Method for getting a favorite song by its name
     *  @param title - The name of the song by which you need to find it in the list of favorite songs
     */

    override fun getFavoriteSongByTitle(title: String): Flow<LocalTrack> {
        return db.favoriteDao().getFavoriteMusicByTitle(title = title).map {
            val song = db.songDao().getSongByTitle(title = it.title)
            mapSongEntityToLocalTrack(
                songEntity = song,
                favoriteEntity = it
            )
        }
    }

}