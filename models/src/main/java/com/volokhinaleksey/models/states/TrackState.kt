package com.volokhinaleksey.models.states

import com.volokhinaleksey.models.local.Track

sealed interface TrackState {

    data class Success(val tracks: List<Track>) : TrackState
    data class Error(val error: String) : TrackState
    object Loading : TrackState

}