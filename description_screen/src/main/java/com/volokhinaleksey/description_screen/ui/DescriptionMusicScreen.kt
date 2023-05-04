package com.volokhinaleksey.description_screen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
