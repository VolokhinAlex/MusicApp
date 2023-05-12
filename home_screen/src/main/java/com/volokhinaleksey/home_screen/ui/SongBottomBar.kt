package com.volokhinaleksey.home_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.volokhinaleksey.core.R
import com.volokhinaleksey.core.ui.themes.Secondary
import com.volokhinaleksey.core.utils.getTrackImageUri
import com.volokhinaleksey.home_screen.viewmodel.HomeScreenViewModel
import com.volokhinaleksey.models.states.UIMusicEvent
import com.volokhinaleksey.models.ui.TrackUI

@Composable
fun SongBottomBar(
    onUIMusicEvent: (UIMusicEvent) -> Unit,
    track: TrackUI,
    songViewModel: HomeScreenViewModel,
    onClickAction: (TrackUI) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 10.dp, end = 10.dp)
            .clickable { onClickAction(track) },
        color = Color.Unspecified
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Secondary)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = getTrackImageUri(track),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = track.title,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    Text(
                        text = if (track.artist == "<unknown>") "Unknown artist" else track.artist,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.55f)
                    )
                }
            }
            Row {
                IconButton(onClick = { onUIMusicEvent(UIMusicEvent.Prev) }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_skip_previous_24),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(onClick = { onUIMusicEvent(UIMusicEvent.PlayPause) }) {
                    Icon(
                        painter = if (songViewModel.isPlaying.value) painterResource(R.drawable.baseline_pause_24) else painterResource(
                            R.drawable.baseline_play_arrow_24
                        ),
                        contentDescription = if (songViewModel.isPlaying.value) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(onClick = { onUIMusicEvent(UIMusicEvent.Next) }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_skip_next_24),
                        contentDescription = "Skip next",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}