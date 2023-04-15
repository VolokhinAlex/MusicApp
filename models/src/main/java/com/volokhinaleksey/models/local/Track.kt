package com.volokhinaleksey.models.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long?,
    val title: String?,
    val album: Album?,
    val artist: String?,
    val duration: Long?,
    val path: String?
): Parcelable