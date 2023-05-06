package com.volokhinaleksey.description_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.core.utils.ALBUM_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_PATH_BUNDLE
import com.volokhinaleksey.interactors.home.MainInteractor
import com.volokhinaleksey.models.states.MediaState
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.UIMusicEvent
import com.volokhinaleksey.models.states.UIState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val REPEAT_MODE_OFF = 0
const val REPEAT_MODE_ONE = 1
const val REPEAT_MODE_ALL = 2

class DescriptionMusicViewModel(
    private val simpleMediaServiceHandler: MusicServiceConnection,
    private val mainInteractor: MainInteractor,
    private val dispatcher: CoroutineDispatcher
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

    private val _currentRepeatMode = mutableStateOf(REPEAT_MODE_OFF)
    val currentRepeatMode: State<Int> get() = _currentRepeatMode

    private val _currentSong = mutableStateOf(TrackUI())
    val currentSong: State<TrackUI> = _currentSong

    private val _songs: MutableLiveData<TrackState> = MutableLiveData()
    val songs: LiveData<TrackState> get() = _songs

    init {
        getSongs()
        viewModelScope.launch {
            simpleMediaServiceHandler.mediaState.collect { mediaState ->
                when (mediaState) {
                    is MediaState.Buffering -> calculateProgressValues(mediaState.progress)
                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.Playing -> _isPlaying.value = mediaState.isPlaying
                    is MediaState.Progress -> calculateProgressValues(mediaState.progress)
                    is MediaState.Ready -> {
                        _currentSong.value = mediaState.trackUI
                        _duration.value = _currentSong.value.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    private fun getSongs() {
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
            _songs.postValue(TrackState.Error(throwable.localizedMessage.orEmpty()))
        }) {
            _songs.postValue(mainInteractor.getSongs())
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    fun onUIEvent(uiMusicEvent: UIMusicEvent) = viewModelScope.launch {
        when (uiMusicEvent) {
            UIMusicEvent.Prev -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Prev)
            UIMusicEvent.Next -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.Next)
            UIMusicEvent.PlayPause -> simpleMediaServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
            is UIMusicEvent.UpdateProgress -> {
                _progress.value = uiMusicEvent.newProgress
                simpleMediaServiceHandler.onPlayerEvent(
                    PlayerEvent.UpdateProgress(
                        uiMusicEvent.newProgress
                    )
                )
            }

            is UIMusicEvent.RepeatMode -> {
                _currentRepeatMode.value = uiMusicEvent.mode
                simpleMediaServiceHandler.onPlayerEvent(
                    PlayerEvent.RepeatMode(currentRepeatMode.value)
                )
            }
        }
    }

    private fun calculateProgressValues(currentProgress: Long) {
        _progress.value =
            if (currentProgress > 0) (currentProgress.toFloat() / _duration.value) else 0f
        _currentDuration.value = currentProgress
    }

    fun loadData(trackUI: List<TrackUI>, currentSongPosition: Int, startDurationMs: Long) {
        simpleMediaServiceHandler.addMediaItemList(
            mediaItemList = trackUI.map {
                val mediaMetaData = MediaMetadata.Builder()
                    .setTitle(it.title)
                    .setArtist(it.artist)
                    .setAlbumTitle(it.albumUI.title)
                    .setExtras(
                        bundleOf(
                            ALBUM_ID_BUNDLE to it.albumUI.id,
                            SONG_PATH_BUNDLE to it.path,
                            SONG_ID_BUNDLE to it.id
                        )
                    ).build()
                val mediaItem =
                    MediaItem.Builder().setUri(it.path).setMediaId(it.id.toString())
                        .setMediaMetadata(mediaMetaData)
                        .setRequestMetadata(
                            MediaItem.RequestMetadata.Builder()
                                .setMediaUri(it.path.toUri())
                                .build()
                        )

                mediaItem.build()
            },
            currentSongPosition = currentSongPosition,
            startDurationMs = startDurationMs
        )
    }

    fun upsertFavoriteSong(trackUI: TrackUI) {
        viewModelScope.launch(dispatcher) {
            mainInteractor.upsertFavoriteSong(trackUI)
        }
    }

    fun getFavoriteSongByTitle(title: String) =
        mainInteractor.getFavoriteSongByTitle(title = title)
}