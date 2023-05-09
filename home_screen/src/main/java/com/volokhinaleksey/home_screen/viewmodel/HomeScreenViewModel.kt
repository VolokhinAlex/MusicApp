package com.volokhinaleksey.home_screen.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.interactors.home.HomeInteractor
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * An object for working with the business logic of the home screen
 */

class HomeScreenViewModel(
    private val homeInteractor: HomeInteractor,
    private val dispatcher: CoroutineDispatcher,
    private val simpleMediaServiceHandler: MusicServiceConnection,
    dataStore: DataStore<Preferences>
) : BaseViewModel<TrackState>(
    simpleMediaServiceHandler = simpleMediaServiceHandler,
    dataStore = dataStore
) {

    init {
        getSongs(query = arrayOf())
    }

    /**
     * Method for getting a list of songs
     * @param query - An additional request for which you need to get certain songs
     */

    fun getSongs(query: Array<String>) {
        mutableData.value = TrackState.Loading
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            mutableData.postValue(TrackState.Error(error = throwable.localizedMessage.orEmpty()))
        }) {
            mutableData.postValue(homeInteractor.getSongs(query = query))
        }
    }

    /**
     * Method for getting a list of favorite songs
     */

    fun getFavoriteSongs() = homeInteractor.getFavoriteSongs()

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }
}