package com.volokhinaleksey.description_screen.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.interactors.description.DescriptionInteractor
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * An object for working with the business logic of the description screen
 */

class DescriptionMusicViewModel(
    private val simpleMediaServiceHandler: MusicServiceConnection,
    private val descriptionInteractor: DescriptionInteractor,
    private val dispatcher: CoroutineDispatcher,
    dataStore: DataStore<Preferences>
) : BaseViewModel<TrackState>(
    simpleMediaServiceHandler = simpleMediaServiceHandler,
    dataStore = dataStore
) {

    init {
        getSongs()
    }

    /**
     * Method for getting a list of songs
     */

    private fun getSongs() {
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            mutableData.postValue(TrackState.Error(throwable.localizedMessage.orEmpty()))
        }) {
            mutableData.postValue(
                descriptionInteractor.getSongs(query = arrayOf())
            )
        }
    }

    /**
     * Method for adding/removing a song from a favorite song list
     */

    fun upsertFavoriteSong(trackUI: TrackUI) {
        viewModelScope.launch(dispatcher) {
            descriptionInteractor.upsertFavoriteSong(trackUI)
        }
    }

    /**
     * Method for getting a favorite song by its name.
     * @param title - The name of the song for which you need to get it from the favorites list
     */

    fun getFavoriteSongByTitle(title: String) =
        descriptionInteractor.getFavoriteSongByTitle(title = title)

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }
}