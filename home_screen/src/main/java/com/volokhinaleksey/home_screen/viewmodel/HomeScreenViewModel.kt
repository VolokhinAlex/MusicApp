package com.volokhinaleksey.home_screen.viewmodel

import androidx.lifecycle.viewModelScope
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.interactors.home.MainInteractor
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val mainInteractor: MainInteractor,
    private val dispatcher: CoroutineDispatcher,
    private val simpleMediaServiceHandler: MusicServiceConnection,
) : BaseViewModel<TrackState>(simpleMediaServiceHandler = simpleMediaServiceHandler) {

    init {
        getSongs(query = arrayOf())
    }

    fun getSongs(query: Array<String>) {
        mutableData.value = TrackState.Loading
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            mutableData.postValue(TrackState.Error(error = throwable.localizedMessage.orEmpty()))
        }) {
            mutableData.postValue(mainInteractor.getSongs(query = query))
        }
    }

    fun getFavoriteSongs() = mainInteractor.getFavoriteSongs()

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }
}