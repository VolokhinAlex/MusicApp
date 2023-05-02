package com.volokhinaleksey.description_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.interactors.home.HomeInteractor
import com.volokhinaleksey.models.states.MediaState
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.UIEvent
import com.volokhinaleksey.models.states.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val REPEAT_MODE_OFF = 0
const val REPEAT_MODE_ONE = 1
const val REPEAT_MODE_ALL = 2

class DescriptionMusicViewModel(
    private val simpleMediaServiceHandler: MusicServiceConnection,
    private val homeInteractor: HomeInteractor
) : ViewModel() {

    private val _duration = mutableStateOf(0L)
    val duration: State<Long> get() = _duration

    private val _progress = mutableStateOf(0f)
    val progress: State<Float> get() = _progress

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private val _currentDuration = mutableStateOf(0L)
    val currentDuration: State<Long> = _currentDuration

    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _songs = MutableLiveData<TrackState>()
    val songs: LiveData<TrackState> get() = _songs

    private val _currentRepeatMode = mutableStateOf(REPEAT_MODE_OFF)
    val currentRepeatMode: State<Int> get() = _currentRepeatMode

    init {
        getSongsFromRepository()
        viewModelScope.launch {
            simpleMediaServiceHandler.mediaState.collect { mediaState ->
                when (mediaState) {
                    is MediaState.Buffering -> calculateProgressValues(mediaState.progress)
                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.Playing -> _isPlaying.value = mediaState.isPlaying
                    is MediaState.Progress -> calculateProgressValues(mediaState.progress)
                    is MediaState.Ready -> {
                        _duration.value = mediaState.track.duration ?: 0
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    private fun getSongsFromRepository() {
        _songs.value = TrackState.Loading
        _songs.value = homeInteractor.getSongs()
    }

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    fun onUIEvent(uiEvent: UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            UIEvent.Prev -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Prev)
            UIEvent.Next -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Next)
            UIEvent.PlayPause -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
            is UIEvent.UpdateProgress -> {
                _progress.value = uiEvent.newProgress
                simpleMediaServiceHandler.onPlayerEvent(
                    PlayerEvent.UpdateProgress(
                        uiEvent.newProgress
                    )
                )
            }

            is UIEvent.RepeatMode -> {
                _currentRepeatMode.value = uiEvent.mode
                simpleMediaServiceHandler.onPlayerEvent(
                    PlayerEvent.RepeatMode(currentRepeatMode.value)
                )
            }

            UIEvent.Shuffle -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Shuffle)
        }
    }

    private fun calculateProgressValues(currentProgress: Long) {
        _progress.value =
            if (currentProgress > 0) (currentProgress.toFloat() / _duration.value) else 0f
        _currentDuration.value = currentProgress
    }

    fun loadData(uri: String) {
        simpleMediaServiceHandler.addMediaItem(MediaItem.fromUri(uri))
    }

}