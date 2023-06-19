package com.volokhinaleksey.core.utils

import androidx.datastore.preferences.core.Preferences
import com.volokhinaleksey.models.local.FavoriteEntity
import com.volokhinaleksey.models.local.LocalAlbum
import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.local.SongEntity
import com.volokhinaleksey.models.ui.AlbumUI
import com.volokhinaleksey.models.ui.TrackUI

fun mapTrackLocalToSongEntity(localTrack: LocalTrack): SongEntity {
    return SongEntity(
        id = localTrack.id ?: 0,
        title = localTrack.title.orEmpty(),
        path = localTrack.path.orEmpty(),
        albumId = localTrack.localAlbum?.id ?: 0,
        albumTitle = localTrack.localAlbum?.title.orEmpty(),
        artist = localTrack.artist.orEmpty(),
        duration = localTrack.duration ?: 0
    )
}

fun mapSongEntityToLocalTrack(songEntity: SongEntity, favoriteEntity: FavoriteEntity?): LocalTrack {
    return LocalTrack(
        id = songEntity.id,
        title = songEntity.title,
        localAlbum = LocalAlbum(id = songEntity.albumId, title = songEntity.albumTitle),
        artist = songEntity.artist,
        duration = songEntity.duration,
        path = songEntity.path,
        isFavorite = favoriteEntity?.isFavorite
    )
}

fun mapSongEntityListToLocalTrackList(
    songEntity: List<SongEntity>,
    favoriteEntity: FavoriteEntity
): List<LocalTrack> {
    return songEntity.map {
        LocalTrack(
            id = it.id,
            title = it.title,
            localAlbum = LocalAlbum(id = it.albumId, title = it.albumTitle),
            artist = it.artist,
            duration = it.duration,
            path = it.path,
            isFavorite = favoriteEntity.isFavorite
        )
    }
}

fun mapLocalTrackToFavoriteEntity(localTrack: LocalTrack): FavoriteEntity {
    return FavoriteEntity(
        title = localTrack.title.orEmpty(),
        isFavorite = localTrack.isFavorite ?: false
    )
}

fun mapTrackUIToLocalTrack(trackUI: TrackUI): LocalTrack {
    return LocalTrack(
        id = trackUI.id,
        title = trackUI.title,
        localAlbum = LocalAlbum(id = trackUI.albumUI.id, title = trackUI.albumUI.title),
        artist = trackUI.artist,
        duration = trackUI.duration,
        path = trackUI.path,
        isFavorite = trackUI.isFavorite
    )
}

fun mapLocalTrackToTrackUI(localTrack: LocalTrack): TrackUI {
    return TrackUI(
        id = localTrack.id ?: 0,
        title = localTrack.title.orEmpty(),
        albumUI = AlbumUI(
            id = localTrack.localAlbum?.id ?: 0,
            title = localTrack.localAlbum?.title.orEmpty()
        ),
        artist = localTrack.artist.orEmpty(),
        duration = localTrack.duration ?: 0,
        path = localTrack.path.orEmpty(),
        isFavorite = localTrack.isFavorite ?: false
    )
}

fun mapDataPreferencesToTrackUI(preferences: Preferences) : TrackUI {
    return TrackUI(
        id = preferences[currentSongIdKey] ?: 0,
        title = preferences[currentSongTitleKey].orEmpty(),
        albumUI = AlbumUI(
            id = preferences[currentSongAlbumIdKey] ?: 0,
            title = preferences[currentSongAlbumTitleKey].orEmpty()
        ),
        artist = preferences[currentSongArtistKey].orEmpty(),
        duration = preferences[currentSongDurationKey] ?: 0,
        path = preferences[currentSongPathKey].orEmpty(),
    )
}