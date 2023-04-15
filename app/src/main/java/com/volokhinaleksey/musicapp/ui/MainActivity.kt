package com.volokhinaleksey.musicapp.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.navigation.parcelable
import com.volokhinaleksey.core.ui.themes.MusicAppTheme
import com.volokhinaleksey.description_screen.ui.DescriptionMusicScreen
import com.volokhinaleksey.home_screen.ui.HomeScreen
import com.volokhinaleksey.models.local.Track

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val readExternalStorage = rememberPermissionState(
                Manifest.permission.READ_EXTERNAL_STORAGE
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val textToShow = if (readExternalStorage.status.shouldShowRationale) {
                        "You must grant permission to read the repository"
                    } else {
                        "Read permission required for this feature to be available. " +
                                "Please grant the permission"
                    }
                    Text(textToShow, fontSize = 25.sp, textAlign = TextAlign.Center)
                    Button(onClick = { readExternalStorage.launchPermissionRequest() }) {
                        Text("Request permission")
                    }
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
            val track = it.arguments?.parcelable<Track>()
            track?.let {
                DescriptionMusicScreen(track = track, navController = navController)
            }
        }
    }
}