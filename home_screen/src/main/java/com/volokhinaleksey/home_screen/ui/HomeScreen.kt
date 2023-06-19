package com.volokhinaleksey.home_screen.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.volokhinaleksey.core.ui.navigation.DATA_KEY
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.navigation.navigate
import com.volokhinaleksey.core.ui.widgets.CardMusic
import com.volokhinaleksey.core.ui.widgets.ErrorMessage
import com.volokhinaleksey.core.ui.widgets.LoadingProgressBar
import com.volokhinaleksey.core.ui.widgets.PagerFavoriteCardMusic
import com.volokhinaleksey.core.ui.widgets.SearchBar
import com.volokhinaleksey.core.ui.widgets.SongPopupMenu
import com.volokhinaleksey.core.ui.widgets.rememberSearchState
import com.volokhinaleksey.home_screen.R
import com.volokhinaleksey.home_screen.viewmodel.HomeScreenViewModel
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.models.states.UIState
import com.volokhinaleksey.models.ui.TrackUI
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val EMPTY_TEXT_FIELD = ""
private val titleTextSize = 25.sp
private val padding20DP = 20.dp

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = koinViewModel(),
    navController: NavController,
    startService: () -> Unit
) {
    val state = rememberSearchState()
    val songs = remember { mutableStateListOf<TrackUI>() }
    Scaffold(
        topBar = {
            SearchBar(
                query = state.query,
                onQueryChange = { state.query = it },
                onSearchFocusChange = { state.focused = it },
                onClearQuery = { state.query = TextFieldValue(EMPTY_TEXT_FIELD) },
                onBack = { state.query = TextFieldValue(EMPTY_TEXT_FIELD) },
                searching = state.searching,
                focused = state.focused,
                modifier = Modifier
            )
            LaunchedEffect(state.query.text) {
                state.searching = true
                delay(300)
                if (state.focused) {
                    homeScreenViewModel.getSongs(arrayOf("%${state.query.text}%"))
                }
                state.searching = false
            }
        },
        bottomBar = {
            val uiState by homeScreenViewModel.uiState.collectAsState()
            if (songs.isNotEmpty()) {
                LaunchedEffect(key1 = true) {
                    homeScreenViewModel.loadData(
                        trackUI = songs,
                        currentSongPosition = songs.indexOfFirst { it.title == homeScreenViewModel.currentSong.value.title },
                        startDurationMs = 0
                    )
                }
            }
            RenderUIState(
                state = uiState,
                songViewModel = homeScreenViewModel,
                navController = navController,
                startService = startService
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            homeScreenViewModel.getFavoriteSongs().collectAsState(initial = emptyList()).value.let {
                RenderFavoriteSongsData(favoriteTracks = it, navController = navController)
            }

            Text(
                text = stringResource(R.string.all_songs),
                color = Color.White,
                fontSize = titleTextSize,
                modifier = Modifier.padding(padding20DP)
            )

            homeScreenViewModel.data.observeAsState().value?.let { trackState ->
                RenderSongsData(state = trackState, navController = navController) {
                    songs.addAll(it)
                }
            }
        }
    }
}

@Composable
private fun RenderUIState(
    state: UIState,
    songViewModel: HomeScreenViewModel,
    navController: NavController,
    startService: () -> Unit
) {
    when (state) {
        UIState.Initial -> {}
        is UIState.Ready -> {
            val track by remember { songViewModel.currentSong }
            LaunchedEffect(true) { startService() }
            SongBottomBar(
                onUIMusicEvent = songViewModel::onUIEvent,
                track = track,
                songViewModel = songViewModel,
                onClickAction = {
                    navController.navigate(
                        route = ScreenState.DescriptionMusicScreen.route,
                        bundleOf(DATA_KEY to it)
                    )
                }
            )
        }
    }
}

@Composable
private fun RenderSongsData(
    state: TrackState,
    navController: NavController,
    tracks: (List<TrackUI>) -> Unit
) {
    when (state) {
        is TrackState.Error -> ErrorMessage(message = state.error)
        TrackState.Loading -> LoadingProgressBar()
        is TrackState.Success -> {
            val songs = state.tracks
            tracks(songs)
            LazyColumn {
                itemsIndexed(songs) { _, item ->
                    var expandedPopupMenu by rememberSaveable { mutableStateOf(false) }
                    CardMusic(
                        track = item,
                        onPopupMenuAction = {
                            expandedPopupMenu = !expandedPopupMenu
                        },
                        onPopupMenu = {
                            SongPopupMenu(
                                state = expandedPopupMenu,
                                track = item,
                                onChangeState = { expandedPopupMenu = false })
                        }, onClick = {
                            navController.navigate(
                                route = ScreenState.DescriptionMusicScreen.route,
                                bundleOf(DATA_KEY to item)
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RenderFavoriteSongsData(
    favoriteTracks: List<TrackUI>,
    navController: NavController
) {
    val pagerState = rememberPagerState()
    if (favoriteTracks.isNotEmpty()) {
        Text(
            text = stringResource(R.string.favorite_songs),
            color = Color.White,
            fontSize = titleTextSize,
            modifier = Modifier.padding(padding20DP)
        )
        HorizontalPager(
            pageCount = favoriteTracks.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { page ->
            PagerFavoriteCardMusic(pagerState, page, favoriteTracks) { track ->
                navController.navigate(
                    route = ScreenState.DescriptionMusicScreen.route,
                    bundleOf(DATA_KEY to track)
                )
            }
        }
    }
}
