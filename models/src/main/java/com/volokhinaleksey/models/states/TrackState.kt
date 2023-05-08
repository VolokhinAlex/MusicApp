package com.volokhinaleksey.models.states

import com.volokhinaleksey.models.ui.TrackUI

sealed interface TrackState {

    data class Success(val tracks: List<TrackUI>) : TrackState
    data class Error(val error: String) : TrackState
    object Loading : TrackState

}