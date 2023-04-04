package com.volokhinaleksey.description_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.core.R

@Composable
fun DescriptionMusicScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, bottom = 20.dp, end = 20.dp, start = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        var sliderPosition by rememberSaveable { mutableStateOf(0f) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = Icons.Filled.KeyboardArrowDown.name,
                    tint = Color.Black
                )
            }

            Text(text = "Music title", fontSize = 18.sp, color = Color.Black)

            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = Icons.Filled.MoreVert.name,
                    tint = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(0.dp, 20.dp)
                .size(300.dp)
                .background(Color.Gray)
                .clip(RoundedCornerShape(10.dp))
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Author name", fontSize = 20.sp, color = Color.Black)
            Text(text = "Music title", fontSize = 25.sp, color = Color.Black)
            Column {
                Slider(value = sliderPosition, onValueChange = { sliderPosition = it })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "00:00", fontSize = 15.sp, color = Color.Black)
                    Text(text = "01:42", fontSize = 15.sp, color = Color.Black)
                }
            }
            Row(modifier = Modifier.padding(0.dp, 20.dp)) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_repeat_24),
                        contentDescription = "Repeat",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_skip_previous_24),
                        contentDescription = "Skip previous",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = Icons.Filled.PlayArrow.name,
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_skip_next_24),
                        contentDescription = "Skip next",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_playlist_play_24),
                        contentDescription = "PlayList",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }

}