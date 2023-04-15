package com.volokhinaleksey.datasource.home

import android.content.Context
import android.provider.MediaStore
import com.volokhinaleksey.models.local.Album
import com.volokhinaleksey.models.local.Track

class HomeLocalDataSourceImpl(
    private val context: Context
) : HomeDataSource {

    override fun getSongs(): List<Track> {
        val songs = mutableListOf<Track>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            StringBuilder("is_music != 0 AND title != ''").toString(),
            null,
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
                    Track(
                        id = id,
                        title = title,
                        album = Album(id = albumId, title = album),
                        artist = artist,
                        duration = duration.toLong(),
                        path = path
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

}