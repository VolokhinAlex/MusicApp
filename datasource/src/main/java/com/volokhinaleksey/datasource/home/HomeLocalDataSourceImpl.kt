package com.volokhinaleksey.datasource.home

import android.content.Context
import android.provider.MediaStore
import com.volokhinaleksey.core.utils.mapSongEntityToLocalTrack
import com.volokhinaleksey.database.MusicDatabase
import com.volokhinaleksey.models.local.FavoriteEntity
import com.volokhinaleksey.models.local.LocalAlbum
import com.volokhinaleksey.models.local.LocalTrack
import com.volokhinaleksey.models.local.SongEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeLocalDataSourceImpl(
    private val context: Context,
    private val db: MusicDatabase
) : HomeDataSource {

    override suspend fun getSongs(query: Array<String>): List<LocalTrack> {
        val songs = mutableListOf<LocalTrack>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            if (query.isEmpty()) "is_music != 0 AND ${MediaStore.Audio.Media.TITLE} != ''" else "is_music != 0 AND ${MediaStore.Audio.Media.TITLE} LIKE ?",
            query,
            "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val duration: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                songs.add(
                    LocalTrack(
                        id = id,
                        title = title,
                        localAlbum = LocalAlbum(id = albumId, title = album),
                        artist = artist,
                        duration = duration.toLong(),
                        path = path,
                        isFavorite = false
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

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

    override suspend fun insertFavoriteSong(favoriteEntity: FavoriteEntity) {
        db.favoriteDao().insert(entity = favoriteEntity)
    }

    override suspend fun insertSong(songEntity: SongEntity) {
        db.songDao().insert(entity = songEntity)
    }

    override suspend fun upsertFavoriteSong(favoriteEntity: FavoriteEntity) {
        db.favoriteDao().upsert(entity = favoriteEntity)
    }

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