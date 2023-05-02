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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.ui.navigation.DATA_KEY
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.navigation.navigate
import com.volokhinaleksey.core.ui.widgets.CardMusic
import com.volokhinaleksey.core.ui.widgets.ErrorMessage
import com.volokhinaleksey.core.ui.widgets.LoadingProgressBar
import com.volokhinaleksey.core.ui.widgets.PagerFavoriteCardMusic
import com.volokhinaleksey.core.ui.widgets.SearchBar
import com.volokhinaleksey.core.ui.widgets.rememberSearchState
import com.volokhinaleksey.home_screen.R
import com.volokhinaleksey.models.local.Track
import com.volokhinaleksey.models.states.TrackState
import org.koin.androidx.compose.koinViewModel

private const val EMPTY_TEXT_FIELD = ""
private val titleTextSize = 25.sp
private val padding20DP = 20.dp

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    homeScreenViewModel: BaseViewModel<TrackState> = koinViewModel(),
    navController: NavController
) {
    val state = rememberSearchState()
    val tracksList = remember {
        mutableStateListOf<Track>()
    }
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
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = stringResource(R.string.favorite_songs),
                color = Color.White,
                fontSize = titleTextSize,
                modifier = Modifier.padding(
                    bottom = padding20DP,
                    start = padding20DP,
                    end = padding20DP
                )
            )

            val pagerState = rememberPagerState()
            HorizontalPager(
                pageCount = tracksList.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp)
            ) { page ->
                PagerFavoriteCardMusic(pagerState, page, tracksList) { track ->
                    navController.navigate(
                        route = ScreenState.DescriptionMusicScreen.route,
                        bundleOf(DATA_KEY to track)
                    )
                }
            }

            Text(
                text = stringResource(R.string.all_songs),
                color = Color.White,
                fontSize = titleTextSize,
                modifier = Modifier.padding(padding20DP)
            )

            homeScreenViewModel.data.observeAsState().value?.let { trackState ->
                RenderData(state = trackState, navController = navController) { tracks ->
                    tracksList.addAll(tracks)
                }
            }
        }
    }
}

@Composable
private fun RenderData(
    state: TrackState,
    navController: NavController,
    tracks: (List<Track>) -> Unit
) {
    when (state) {
        is TrackState.Error -> ErrorMessage(message = state.error)
        TrackState.Loading -> LoadingProgressBar()
        is TrackState.Success -> {
            val songs = state.tracks
            tracks(songs)
            LazyColumn {
                itemsIndexed(songs) { _, item ->
                    CardMusic(item) {
                        navController.navigate(
                            route = ScreenState.DescriptionMusicScreen.route,
                            bundleOf(DATA_KEY to item)
                        )
                    }
                }
            }
        }
    }
}
