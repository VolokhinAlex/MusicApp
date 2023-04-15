package com.volokhinaleksey.interactors.home

import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.repositories.HomeRepository

class HomeInteractorImpl(
    private val repository: HomeRepository
) : HomeInteractor {

    override fun getSongs(): TrackState {
        return TrackState.Success(repository.getSongs())
    }

}