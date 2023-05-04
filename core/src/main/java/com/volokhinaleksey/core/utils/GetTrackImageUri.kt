package com.volokhinaleksey.core.utils

import android.content.ContentUris
import android.net.Uri
import com.volokhinaleksey.models.local.Track

fun getTrackImageUri(track: Track): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        track.album?.id ?: -1
    )
}