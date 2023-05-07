package com.volokhinaleksey.description_screen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.core.R
import com.volokhinaleksey.core.utils.REPEAT_MODE_ALL
import com.volokhinaleksey.core.utils.REPEAT_MODE_OFF
import com.volokhinaleksey.core.utils.REPEAT_MODE_ONE
import com.volokhinaleksey.core.utils.durationConvert
import com.volokhinaleksey.description_screen.viewmodel.DescriptionMusicViewModel
import com.volokhinaleksey.models.states.UIMusicEvent
import com.volokhinaleksey.models.ui.TrackUI

@SuppressLint("PrivateResource")
@Composable
fun BottomControls(
    progress: Float,
    duration: Long,
    songViewModel: DescriptionMusicViewModel,
    onUIMusicEvent: (UIMusicEvent) -> Unit,
    currentTime: () -> Long
) {
    var newProgressValue by rememberSaveable { mutableStateOf(0f) }
    var useNewProgressValue by rememberSaveable { mutableStateOf(false) }
    val musicTime = remember(currentTime()) { currentTime() }
    Column {
        Slider(
            value = if (useNewProgressValue) newProgressValue else progress,
            onValueChange = {
                useNewProgressValue = true
                newProgressValue = it
                onUIMusicEvent(UIMusicEvent.UpdateProgress(newProgress = it))
            },
            onValueChangeFinished = {
                useNewProgressValue = false
            },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = durationConvert(musicTime),
                fontSize = 15.sp,
                color = Color.White
            )
            Text(
                text = durationConvert(duration),
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
    Row(modifier = Modifier.padding(0.dp, 20.dp)) {
        IconButton(
            onClick = {
                when (songViewModel.currentRepeatMode.value) {
                    REPEAT_MODE_ONE -> {
                        onUIMusicEvent(UIMusicEvent.RepeatMode(mode = REPEAT_MODE_OFF))
                    }

                    REPEAT_MODE_ALL -> {
                        onUIMusicEvent(UIMusicEvent.RepeatMode(mode = REPEAT_MODE_ONE))
                    }

                    else -> {
                        onUIMusicEvent(UIMusicEvent.RepeatMode(mode = REPEAT_MODE_ALL))
                    }
                }
            },
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                painter = when (songViewModel.currentRepeatMode.value) {
                    REPEAT_MODE_ONE -> {
                        painterResource(androidx.media3.ui.R.drawable.exo_icon_repeat_one)
                    }

                    REPEAT_MODE_ALL -> {
                        painterResource(androidx.media3.ui.R.drawable.exo_icon_repeat_all)
                    }

                    else -> {
                        painterResource(androidx.media3.ui.R.drawable.exo_icon_repeat_off)
                    }
                },
                contentDescription = stringResource(com.volokhinaleksey.description_screen.R.string.repeat),
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(
            onClick = { onUIMusicEvent(UIMusicEvent.Prev) },
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_previous_24),
                contentDescription = stringResource(com.volokhinaleksey.description_screen.R.string.skip_previous),
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        IconButton(
            onClick = {
                onUIMusicEvent(UIMusicEvent.PlayPause)
            },
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                painter = if (songViewModel.isPlaying.value) painterResource(R.drawable.baseline_pause_24) else painterResource(
                    R.drawable.baseline_play_arrow_24
                ),
                contentDescription = if (songViewModel.isPlaying.value) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        IconButton(
            onClick = {
                onUIMusicEvent(UIMusicEvent.Next)
            },
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_next_24),
                contentDescription = "Skip next",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        val favoriteObject by songViewModel.getFavoriteSongByTitle(title = songViewModel.currentSong.value.title)
            .collectAsState(initial = TrackUI())
        IconButton(
            onClick = {
                songViewModel.upsertFavoriteSong(trackUI = favoriteObject.copy(isFavorite = !favoriteObject.isFavorite))
            }
        ) {
            Icon(
                imageVector = if (favoriteObject.isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = "Add Or Remove favorite song",
                tint = if (favoriteObject.isFavorite) Color.Red else Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}