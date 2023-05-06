package com.volokhinaleksey.home_screen.viewmodel

import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.interactors.home.MainInteractor
import com.volokhinaleksey.models.states.TrackState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val mainInteractor: MainInteractor,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<TrackState>() {

    init {
        getSongs()
    }

    private fun getSongs() {
        mutableData.value = TrackState.Loading
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            mutableData.postValue(TrackState.Error(error = throwable.localizedMessage.orEmpty()))
        }) {
            mutableData.postValue(mainInteractor.getSongs())
        }
    }

    fun getFavoriteSongs() = mainInteractor.getFavoriteSongs()

}