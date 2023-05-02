package com.volokhinaleksey.models.states

import com.volokhinaleksey.models.local.Track

sealed class MediaState {
    object Initial : MediaState()
    data class Ready(val track: Track) : MediaState()
    data class Progress(val progress: Long) : MediaState()
    data class Buffering(val progress: Long) : MediaState()
    data class Playing(val isPlaying: Boolean) : MediaState()
}