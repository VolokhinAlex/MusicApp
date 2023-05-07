package com.volokhinaleksey.core.utils

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val ALBUM_ID_BUNDLE = "album_id"
const val SONG_PATH_BUNDLE = "song_path"
const val SONG_ID_BUNDLE = "song_id"

// Database
const val MUSIC_DATABASE_NAME = "music_database.db"

// Notification
const val CHANNEL_ID = "Music Player"
const val NOTIFICATION_ID = 200
const val NAME_NOTIFICATION = "Music Player"

const val REPEAT_MODE_OFF = 0
const val REPEAT_MODE_ONE = 1
const val REPEAT_MODE_ALL = 2

// Datastore
const val MUSIC_PREFERENCES = "MusicPreferences"

val currentSongPositionKey = longPreferencesKey("currentPosition")
val currentSongTitleKey = stringPreferencesKey("title")
val currentSongIdKey = longPreferencesKey("id")
val currentSongAlbumIdKey = longPreferencesKey("albumId")
val currentSongAlbumTitleKey = stringPreferencesKey("albumTitle")
val currentSongArtistKey = stringPreferencesKey("artist")
val currentSongDurationKey = longPreferencesKey("duration")
val currentSongPathKey = stringPreferencesKey("path")