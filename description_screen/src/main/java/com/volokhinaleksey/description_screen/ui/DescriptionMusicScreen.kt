package com.volokhinaleksey.description_screen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionMusicScreen() {

    Box(contentAlignment = Alignment.Center) {
        Text(text = "Hello From DescriptionMusicScreen", fontSize = 25.sp, color = Color.Black)
    }

}