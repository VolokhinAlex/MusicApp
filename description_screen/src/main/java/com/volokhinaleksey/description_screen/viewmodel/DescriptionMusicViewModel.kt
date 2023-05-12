package com.volokhinaleksey.description_screen.viewmodel

import androidx.lifecycle.viewModelScope
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.interactors.home.MainInteractor
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class DescriptionMusicViewModel(
    private val simpleMediaServiceHandler: MusicServiceConnection,
    private val mainInteractor: MainInteractor,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<TrackState>(simpleMediaServiceHandler = simpleMediaServiceHandler) {

    init {
        getSongs()
    }

    private fun getSongs() {
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            mutableData.postValue(TrackState.Error(throwable.localizedMessage.orEmpty()))
        }) {
            mutableData.postValue(
                mainInteractor.getSongs(query = arrayOf())
            )
        }
    }

    fun upsertFavoriteSong(trackUI: TrackUI) {
        viewModelScope.launch(dispatcher) {
            mainInteractor.upsertFavoriteSong(trackUI)
        }
    }

    fun getFavoriteSongByTitle(title: String) =
        mainInteractor.getFavoriteSongByTitle(title = title)

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }
}