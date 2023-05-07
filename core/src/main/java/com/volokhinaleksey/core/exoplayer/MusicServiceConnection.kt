package com.volokhinaleksey.core.exoplayer

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.volokhinaleksey.core.utils.ALBUM_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_PATH_BUNDLE
import com.volokhinaleksey.models.states.MediaState
import com.volokhinaleksey.models.states.PlayerEvent
import com.volokhinaleksey.models.ui.AlbumUI
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicServiceConnection(
    private val player: Player
) : Player.Listener {

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Initial)
    val mediaState = _mediaState.asStateFlow()

//    private val dataStore: DataStore<Preferences> by inject()

    private var job: Job? = null

//    private val currentPosition: Flow<Long> by lazy {
//        dataStore.data.map {
//            it[currentSongPositionKey] ?: 0
//        }
//    }
//
//    private val currentTrack: Flow<String> by lazy {
//        dataStore.data.map {
//            it[currentSongTitleKey] ?: ""
//        }
//    }

    private var currentPosition = 0L
    private var currentTrack = TrackUI()

    init {
        player.addListener(this)
        job = Job()
    }

    fun addMediaItemList(
        mediaItemList: List<MediaItem>,
        currentSongPosition: Int,
        startDurationMs: Long
    ) {
        player.setMediaItems(mediaItemList, currentSongPosition, startDurationMs)
        player.prepare()
    }

    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Prev -> player.seekToPrevious()
            PlayerEvent.Next -> player.seekToNext()
            PlayerEvent.PlayPause -> {
                if (player.isPlaying) {
                    player.pause()
                    stopProgressUpdate()
                } else {
                    player.play()
                    _mediaState.value = MediaState.Playing(isPlaying = true)
                    startProgressUpdate()
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> player.seekTo((player.duration * playerEvent.newProgress).toLong())
            is PlayerEvent.RepeatMode -> player.repeatMode = playerEvent.mode
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _mediaState.value =
                MediaState.Buffering(player.currentPosition)

            ExoPlayer.STATE_READY -> _mediaState.value = MediaState.Ready(
                TrackUI(
                    id = player.mediaMetadata.extras?.getLong(SONG_ID_BUNDLE) ?: 0,
                    title = player.mediaMetadata.title?.toString() ?: "",
                    albumUI = AlbumUI(
                        id = player.mediaMetadata.extras?.getLong(ALBUM_ID_BUNDLE) ?: 0,
                        title = player.mediaMetadata.albumTitle?.toString() ?: ""
                    ),
                    artist = player.mediaMetadata.artist?.toString() ?: "",
                    duration = player.duration,
                    path = player.mediaMetadata.extras?.getString(SONG_PATH_BUNDLE) ?: ""
                )
            )

            else -> super.onPlaybackStateChanged(playbackState)
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        val track = TrackUI(
            id = mediaMetadata.extras?.getLong(SONG_ID_BUNDLE) ?: 0,
            title = mediaMetadata.title?.toString() ?: "",
            albumUI = AlbumUI(
                id = mediaMetadata.extras?.getLong(ALBUM_ID_BUNDLE) ?: 0,
                title = mediaMetadata.albumTitle?.toString() ?: ""
            ),
            artist = mediaMetadata.artist?.toString() ?: "",
            duration = player.duration,
            path = mediaMetadata.extras?.getString(SONG_PATH_BUNDLE) ?: ""
        )
        if (currentTrack.title == track.title && currentPosition != 0L) {
            player.seekTo(currentPosition)
        }
        _mediaState.value = MediaState.Ready(track)
        currentTrack = track
//        saveCurrentMusicData(trackUI = track)
        super.onMediaMetadataChanged(mediaMetadata)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _mediaState.value = MediaState.Playing(isPlaying = isPlaying)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
        super.onIsPlayingChanged(isPlaying)
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    private fun saveCurrentMusicData(trackUI: TrackUI) {
//        GlobalScope.launch {
//            dataStore.edit { preferences ->
//                preferences[currentSongTitleKey] = trackUI.title
//                preferences[currentSongIdKey] = trackUI.id
//                preferences[currentSongAlbumIdKey] = trackUI.albumUI.id
//                preferences[currentSongAlbumTitleKey] = trackUI.albumUI.title
//                preferences[currentSongArtistKey] = trackUI.artist
//                preferences[currentSongDurationKey] = trackUI.duration
//                preferences[currentSongPathKey] = trackUI.path
//            }
//        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    private fun saveCurrentMusicPosition(currentSongPosition: Long) {
//        GlobalScope.launch {
//            dataStore.edit { preferences ->
//                preferences[currentSongPositionKey] = currentSongPosition
//            }
//        }
//    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            //saveCurrentMusicPosition(currentSongPosition = player.currentPosition)
            currentPosition = player.currentPosition
            _mediaState.value = MediaState.Progress(player.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _mediaState.value = MediaState.Playing(isPlaying = false)
    }
}