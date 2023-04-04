package com.volokhinaleksey.home_screen.viewmodel

import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.interactors.home.HomeInteractor
import com.volokhinaleksey.models.states.TrackState

class HomeScreenViewModel(
    private val homeInteractor: HomeInteractor
) : BaseViewModel<TrackState>() {

    init {
        getSongsFromRepository()
    }

    private fun getSongsFromRepository() {
        mutableData.value = TrackState.Loading
        mutableData.value = homeInteractor.getSongs()
    }

}