package com.volokhinaleksey.core.utils

import android.content.ContentUris
import android.net.Uri
import com.volokhinaleksey.models.ui.TrackUI

fun getTrackImageUri(track: TrackUI): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        track.albumUI.id
    )
}