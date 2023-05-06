package com.volokhinaleksey.models.states

import com.volokhinaleksey.models.ui.TrackUI

sealed class MediaState {
    object Initial : MediaState()
    data class Ready(val trackUI: TrackUI) : MediaState()
    data class Progress(val progress: Long) : MediaState()
    data class Buffering(val progress: Long) : MediaState()
    data class Playing(val isPlaying: Boolean) : MediaState()
}