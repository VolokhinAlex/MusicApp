package com.volokhinaleksey.core.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.core.utils.ALBUM_ID_BUNDLE
import com.volokhinaleksey.core.utils.REPEAT_MODE_OFF
import com.volokhinaleksey.core.utils.SONG_DURATION_BUNDLE
import com.volokhinaleksey.core.utils.SONG_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_PATH_BUNDLE
import com.volokhinaleksey.core.utils.currentSongAlbumIdKey
import com.volokhinaleksey.core.utils.currentSongAlbumTitleKey
import com.volokhinaleksey.core.utils.currentSongArtistKey
import com.volokhinaleksey.core.utils.currentSongDurationKey
import com.volokhinaleksey.core.utils.currentSongIdKey
import com.volokhinaleksey.core.utils.currentSongPathKey
import com.volokhinaleksey.core.utils.currentSongTitleKey
import com.volokhinaleksey.core.utils.mapDataPreferencesToTrackUI
import com.volokhinaleksey.models.states.MediaState
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.states.UIMusicEvent
import com.volokhinaleksey.models.states.UIState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : Any>(
    private val simpleMediaServiceHandler: MusicServiceConnection,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    protected val mutableData: MutableLiveData<T> = MutableLiveData()
    val data: LiveData<T> get() = mutableData

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

    init {
        getTrackData()
        viewModelScope.launch {
            simpleMediaServiceHandler.mediaState.collect { mediaState ->
                when (mediaState) {
                    is MediaState.Buffering -> calculateProgressValues(currentProgress = mediaState.progress)
                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.Playing -> _isPlaying.value = mediaState.isPlaying
                    is MediaState.Progress -> calculateProgressValues(currentProgress = mediaState.progress)
                    is MediaState.Ready -> {
                        _currentSong.value = mediaState.trackUI
                        saveTrackData(track = mediaState.trackUI)
                        _duration.value = _currentSong.value.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    private fun calculateProgressValues(currentProgress: Long) {
        _progress.value =
            if (currentProgress > 0) (currentProgress.toFloat() / _duration.value) else 0f
        _currentDuration.value = currentProgress
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
                            SONG_ID_BUNDLE to it.id,
                            SONG_DURATION_BUNDLE to it.duration
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

    private suspend fun saveTrackData(track: TrackUI) {
        dataStore.edit { preferences ->
            preferences[currentSongTitleKey] = track.title
            preferences[currentSongIdKey] = track.id
            preferences[currentSongAlbumIdKey] = track.albumUI.id
            preferences[currentSongAlbumTitleKey] = track.albumUI.title
            preferences[currentSongArtistKey] = track.artist
            preferences[currentSongDurationKey] = track.duration
            preferences[currentSongPathKey] = track.path
        }
    }

    private fun getTrackData() {
        viewModelScope.launch {
            dataStore.data.map { preferences -> mapDataPreferencesToTrackUI(preferences = preferences) }
                .collect {
                    _currentSong.value = it
                }
        }
    }
}