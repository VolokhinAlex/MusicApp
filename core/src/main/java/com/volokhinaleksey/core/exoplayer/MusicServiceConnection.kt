package com.volokhinaleksey.core.exoplayer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.volokhinaleksey.core.utils.ALBUM_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_DURATION_BUNDLE
import com.volokhinaleksey.core.utils.SONG_ID_BUNDLE
import com.volokhinaleksey.core.utils.SONG_PATH_BUNDLE
import com.volokhinaleksey.core.utils.currentSongPositionKey
import com.volokhinaleksey.core.utils.mapDataPreferencesToTrackUI
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(DelicateCoroutinesApi::class)
class MusicServiceConnection(
    private val player: Player
) : Player.Listener, KoinComponent {

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Initial)
    val mediaState = _mediaState.asStateFlow()

    private val datastore: DataStore<Preferences> by inject()

    private var job: Job? = null

    private var currentPosition = 0L
    private var currentTrack = TrackUI()
    private var isCurrentTrackPlaying = false

    init {
        GlobalScope.launch {
            val currentTrackData =
                datastore.data.map { mapDataPreferencesToTrackUI(preferences = it) }.first()
            val currentSongPosition = datastore.data.map {
                it[currentSongPositionKey]
            }.first()
            currentSongPosition?.let {
                if (it != 0L) {
                    currentPosition = it
                }
            }
            currentTrack = currentTrackData
        }
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
        val track =
            TrackUI(
                id = mediaMetadata.extras?.getLong(SONG_ID_BUNDLE) ?: 0,
                title = mediaMetadata.title?.toString() ?: "",
                albumUI = AlbumUI(
                    id = mediaMetadata.extras?.getLong(ALBUM_ID_BUNDLE) ?: 0,
                    title = mediaMetadata.albumTitle?.toString() ?: ""
                ),
                artist = mediaMetadata.artist?.toString() ?: "",
                duration = mediaMetadata.extras?.getLong(SONG_DURATION_BUNDLE) ?: player.duration,
                path = mediaMetadata.extras?.getString(SONG_PATH_BUNDLE) ?: ""
            )
        _mediaState.value = MediaState.Ready(track)
        if (currentTrack.title == track.title && currentPosition != 0L) {
            player.seekTo(currentPosition)
        } else {
            currentPosition = 0L
        }
        currentTrack = track
        super.onMediaMetadataChanged(mediaMetadata)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        isCurrentTrackPlaying = isPlaying
        _mediaState.value = MediaState.Playing(isPlaying = isCurrentTrackPlaying)
        if (isCurrentTrackPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
        super.onIsPlayingChanged(isCurrentTrackPlaying)
    }

    private suspend fun startProgressUpdate() = job.run {
        while (isCurrentTrackPlaying) {
            delay(500)
            _mediaState.value = MediaState.Playing(isPlaying = isCurrentTrackPlaying)
            currentPosition = player.currentPosition
            _mediaState.value = MediaState.Progress(player.currentPosition)
            datastore.edit {
                it[currentSongPositionKey] = player.currentPosition
            }
        }
    }

    private fun stopProgressUpdate() {
        isCurrentTrackPlaying = false
        job?.cancel()
        _mediaState.value = MediaState.Playing(isPlaying = isCurrentTrackPlaying)
    }
}