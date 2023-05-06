package com.volokhinaleksey.core.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.volokhinaleksey.core.utils.getTrackImageUri
import com.volokhinaleksey.models.ui.TrackUI

@Composable
fun CardMusic(track: TrackUI, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onActionClick.invoke() }
            .padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            AsyncImage(
                model = getTrackImageUri(track), contentDescription = "", modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = track.title, fontSize = 18.sp, color = Color.White, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.65f)
                )
                Text(
                    text = if (track.artist == "<unknown>") "Unknown artist" else track.artist,
                    fontSize = 18.sp,
                    color = Color.White, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.65f)
                )
            }
        }
        IconButton(
            onClick = { },
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = Icons.Filled.MoreVert.name,
                tint = Color.White
            )
        }
    }
}