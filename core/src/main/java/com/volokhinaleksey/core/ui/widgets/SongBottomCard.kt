package com.volokhinaleksey.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.volokhinaleksey.core.utils.getTrackImageUri
import com.volokhinaleksey.models.ui.TrackUI

@Composable
fun SongBottomCard(tracks: List<TrackUI>, isPlaying: () -> Boolean) {
    val playing = remember(isPlaying()) { isPlaying() }
    LazyRow {
        itemsIndexed(tracks) { _, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.LightGray)
                    .padding(10.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = getTrackImageUri(item),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = item.title, color = Color.White, fontSize = 22.sp)
                        Text(text = item.artist, color = Color.White, fontSize = 18.sp)
                    }
                }
                IconButton(onClick = {
                    //exoPlayer.setMediaItem(MediaItem.fromUri(item.path))
                    if (playing) {
                        //    exoPlayer.stop()
                    } else {
                        //   exoPlayer.play()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = Icons.Filled.PlayArrow.name,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}