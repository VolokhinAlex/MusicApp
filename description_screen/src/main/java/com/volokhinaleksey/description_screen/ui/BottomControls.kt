package com.volokhinaleksey.description_screen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.volokhinaleksey.core.utils.durationConvert
import com.volokhinaleksey.description_screen.viewmodel.DescriptionMusicViewModel
import com.volokhinaleksey.description_screen.viewmodel.REPEAT_MODE_ALL
import com.volokhinaleksey.description_screen.viewmodel.REPEAT_MODE_OFF
import com.volokhinaleksey.description_screen.viewmodel.REPEAT_MODE_ONE
import com.volokhinaleksey.models.states.UIEvent

@SuppressLint("PrivateResource")
@Composable
fun BottomControls(
    progress: Float,
    duration: Long,
    songViewModel: DescriptionMusicViewModel,
    onUiEvent: (UIEvent) -> Unit,
    currentTime: () -> Long
) {
    var shuffleMode by rememberSaveable {
        mutableStateOf(false)
    }
    var newProgressValue by remember { mutableStateOf(0f) }
    var useNewProgressValue by remember { mutableStateOf(false) }
    val musicTime = remember(currentTime()) { currentTime() }

    Column {
        Slider(
            value = if (useNewProgressValue) newProgressValue else progress,
            onValueChange = {
                useNewProgressValue = true
                newProgressValue = it
                onUiEvent(UIEvent.UpdateProgress(newProgress = it))
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
                        onUiEvent(UIEvent.RepeatMode(mode = REPEAT_MODE_OFF))
                    }

                    REPEAT_MODE_ALL -> {
                        onUiEvent(UIEvent.RepeatMode(mode = REPEAT_MODE_ONE))
                    }

                    else -> {
                        onUiEvent(UIEvent.RepeatMode(mode = REPEAT_MODE_ALL))
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
            onClick = { onUiEvent(UIEvent.Prev) },
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
                onUiEvent(UIEvent.PlayPause)
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
                onUiEvent(UIEvent.Next)
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

        IconButton(
            onClick = {
                onUiEvent(UIEvent.Shuffle)
                shuffleMode = !shuffleMode
            }
        ) {
            Icon(
                painter = if (shuffleMode) {
                    painterResource(androidx.media3.ui.R.drawable.exo_icon_shuffle_on)
                } else {
                    painterResource(
                        androidx.media3.ui.R.drawable.exo_icon_shuffle_off
                    )
                },
                contentDescription = "Shuffle",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}