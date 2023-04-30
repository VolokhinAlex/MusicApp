package com.volokhinaleksey.description_screen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.volokhinaleksey.core.R
import com.volokhinaleksey.core.utils.durationConvert
import com.volokhinaleksey.core.utils.getTrackImageUri
import com.volokhinaleksey.description_screen.viewmodel.DescriptionMusicViewModel
import com.volokhinaleksey.models.local.Track
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.UIEvent
import com.volokhinaleksey.models.states.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DescriptionMusicScreen(
    track: Track,
    navController: NavController,
    songViewModel: DescriptionMusicViewModel = koinViewModel(),
    startService: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        songViewModel.loadData(track.path.orEmpty())
        songViewModel.onUIEvent(UIEvent.PlayPause)
    }
    val songs = remember {
        mutableStateListOf<Track>()
    }
    val state = songViewModel.uiState.collectAsState()
    songViewModel.songs.observeAsState().value?.let {
        when (it) {
            is TrackState.Success -> {
                songs.addAll(it.tracks)
            }

            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, bottom = 20.dp, end = 20.dp, start = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (state.value) {
            UIState.Initial -> {}
            is UIState.Ready -> {
                LaunchedEffect(true) {
                    startService()
                }
                TopBarControls(navController = navController, track = track)
                TrackImage(track = track)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TrackInformation(track = track)
                    BottomControls(
                        duration = songViewModel.duration.value,
                        songViewModel = songViewModel,
                        onUiEvent = songViewModel::onUIEvent,
                        currentTime = { songViewModel.currentDuration.value },
                        progress = songViewModel.progress.value
                    )
                }
            }
        }

    }
}

@Composable
fun TopBarControls(navController: NavController, track: Track) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = Icons.Filled.KeyboardArrowDown.name,
                tint = Color.White
            )
        }

        Text(
            text = "${track.title}", fontSize = 18.sp, color = Color.White, maxLines = 1,
            overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.7f),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = Icons.Filled.MoreVert.name,
                tint = Color.White
            )
        }
    }
}

@Composable
fun TrackImage(track: Track) {
    AsyncImage(
        model = getTrackImageUri(track),
        contentDescription = "Music Picture",
        modifier = Modifier
            .padding(0.dp, 20.dp)
            .size(300.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}

@Composable
fun TrackInformation(track: Track) {
    Text(
        text = "${track.title}",
        fontSize = 25.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    )
    Text(
        text = if (track.artist.equals("<unknown>")) "Unknown artist" else "${track.artist}",
        fontSize = 20.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun BottomControls(
    progress: Float,
    duration: Long,
    songViewModel: DescriptionMusicViewModel,
    onUiEvent: (UIEvent) -> Unit,
    currentTime: () -> Long
) {
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
            onClick = {},
            modifier = Modifier.padding(end = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_repeat_one_24),
                contentDescription = "Repeat",
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
                contentDescription = "Skip previous",
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
            onClick = { onUiEvent(UIEvent.Prev) }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_shuffle_24),
                contentDescription = "Shuffle",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}