package com.volokhinaleksey.models.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Long,
    val title: String
): Parcelable
