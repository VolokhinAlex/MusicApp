package com.volokhinaleksey.repositories

import com.volokhinaleksey.core.utils.mapLocalTrackToFavoriteEntity
import com.volokhinaleksey.core.utils.mapLocalTrackToTrackUI
import com.volokhinaleksey.core.utils.mapTrackLocalToSongEntity
import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainRepositoryImpl(
    private val homeDataSource: HomeDataSource
) : MainRepository {
    override suspend fun getSongs(): List<LocalTrack> {
        return homeDataSource.getSongs().map {
            homeDataSource.insertSong(mapTrackLocalToSongEntity(localTrack = it))
            homeDataSource.insertFavoriteSong(mapLocalTrackToFavoriteEntity(localTrack = it))
            it
        }
    }

    override fun getFavoriteSongs(): Flow<List<LocalTrack>> {
        return homeDataSource.getFavoriteSongs()
    }

    override suspend fun upsertFavoriteSong(localTrack: LocalTrack) {
        homeDataSource.upsertFavoriteSong(mapLocalTrackToFavoriteEntity(localTrack = localTrack))
    }

    override fun getFavoriteSongByTitle(title: String): Flow<TrackUI> {
        return homeDataSource.getFavoriteSongByTitle(title = title).map {
            mapLocalTrackToTrackUI(localTrack = it)
        }
    }


}