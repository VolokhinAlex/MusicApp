package com.volokhinaleksey.musicapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.themes.MusicAppTheme
import com.volokhinaleksey.description_screen.ui.DescriptionMusicScreen
import com.volokhinaleksey.home_screen.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenState.HomeScreen.route
    ) {
        composable(route = ScreenState.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = ScreenState.DescriptionMusicScreen.route) {
            DescriptionMusicScreen()
        }
    }
}