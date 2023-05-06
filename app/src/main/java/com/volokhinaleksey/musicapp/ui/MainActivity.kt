package com.volokhinaleksey.musicapp.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.volokhinaleksey.core.exoplayer.MusicService
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.navigation.parcelable
import com.volokhinaleksey.core.ui.themes.MusicAppTheme
import com.volokhinaleksey.description_screen.ui.DescriptionMusicScreen
import com.volokhinaleksey.home_screen.ui.HomeScreen
import com.volokhinaleksey.models.ui.TrackUI

class MainActivity : ComponentActivity() {

    private var isServiceRunning = false

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val readExternalStorage =
                rememberPermissionState(
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) Manifest.permission.READ_MEDIA_AUDIO
                    else Manifest.permission.READ_EXTERNAL_STORAGE
                )
            if (readExternalStorage.status.isGranted) {
                MusicAppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Navigation()
                    }
                }
            } else {
                LaunchedEffect(key1 = true) {
                    readExternalStorage.launchPermissionRequest()
                }
            }
        }
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(this, MusicService::class.java)
            startForegroundService(intent)
            isServiceRunning = true
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
                val trackUI = it.arguments?.parcelable<TrackUI>()
                trackUI?.let {
                    DescriptionMusicScreen(
                        track = trackUI,
                        navController = navController,
                        startService = { startService() })
                }
            }
        }
    }
}