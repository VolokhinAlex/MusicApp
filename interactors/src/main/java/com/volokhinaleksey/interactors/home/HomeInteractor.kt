package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.models.states.TrackState

interface HomeInteractor {
    fun getSongs(): TrackState

}