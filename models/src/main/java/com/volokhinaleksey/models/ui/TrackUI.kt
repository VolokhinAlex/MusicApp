package com.volokhinaleksey.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackUI(
    val id: Long = 0,
    val title: String = "",
    val albumUI: AlbumUI = AlbumUI(),
    val artist: String = "",
    val duration: Long = 0,
    val path: String = "",
    val isFavorite: Boolean = false
) : Parcelable
