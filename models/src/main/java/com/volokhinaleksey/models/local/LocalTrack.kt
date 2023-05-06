package com.volokhinaleksey.models.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalTrack(
    val id: Long?,
    val title: String?,
    val localAlbum: LocalAlbum?,
    val artist: String?,
    val duration: Long?,
    val path: String?,
    val isFavorite: Boolean?
): Parcelable