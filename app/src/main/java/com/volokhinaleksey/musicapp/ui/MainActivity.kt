package com.volokhinaleksey.musicapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.themes.MusicAppTheme
import com.volokhinaleksey.description_screen.ui.DescriptionMusicScreen
import com.volokhinaleksey.favorite_screen.ui.FavoriteScreen
import com.volokhinaleksey.home_screen.ui.HomeScreen
import com.volokhinaleksey.search_screen.ui.SearchScreen

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
    BottomNavigationBar(navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenState.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ScreenState.HomeScreen.route) {
                HomeScreen()
            }
            composable(route = ScreenState.SearchScreen.route) {
                SearchScreen()
            }
            composable(route = ScreenState.FavoriteScreen.route) {
                FavoriteScreen()
            }
            composable(route = ScreenState.DescriptionMusicScreen.route) {
                DescriptionMusicScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val bottomNavItems =
        listOf(ScreenState.HomeScreen, ScreenState.SearchScreen, ScreenState.FavoriteScreen)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != ScreenState.DescriptionMusicScreen.route) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = item.route == currentRoute
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigate(item.route) },
                            label = {
                                Text(
                                    text = stringResource(id = item.label),
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else Color.Unspecified
                                )
                            },
                            icon = {
                                item.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = stringResource(id = item.label),
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
        content = content
    )
}