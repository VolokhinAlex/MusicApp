package com.volokhinaleksey.home_screen.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.ui.navigation.DATA_KEY
import com.volokhinaleksey.core.ui.navigation.ScreenState
import com.volokhinaleksey.core.ui.navigation.navigate
import com.volokhinaleksey.core.ui.widgets.CardMusic
import com.volokhinaleksey.core.ui.widgets.SearchBar
import com.volokhinaleksey.core.ui.widgets.rememberSearchState
import com.volokhinaleksey.models.states.TrackState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    homeScreenViewModel: BaseViewModel<TrackState> = koinViewModel(),
    navController: NavController
) {
    val state = rememberSearchState()
    Column {

        SearchBar(
            query = state.query,
            onQueryChange = { state.query = it },
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            onBack = { state.query = TextFieldValue("") },
            searching = state.searching,
            focused = state.focused
        )

        Text(
            text = "Favorite Songs",
            color = Color.Black,
            fontSize = 25.sp,
            modifier = Modifier.padding(20.dp)
        )

        val pagerState = rememberPagerState()
        HorizontalPager(
            pageCount = 4,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { page ->
            PagerTrendyCardMusic(pagerState, page)
        }

        Text(
            text = "All songs",
            color = Color.Black,
            fontSize = 25.sp,
            modifier = Modifier.padding(20.dp)
        )

        homeScreenViewModel.data.observeAsState().value?.let {
            RenderData(state = it, navController = navController)
        }
    }
}

@Composable
fun RenderData(state: TrackState, navController: NavController) {
    when (state) {
        is TrackState.Error -> {}
        TrackState.Loading -> {}
        is TrackState.Success -> {
            val songs = state.tracks
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTrendyCardMusic(pagerState: PagerState, page: Int) {
    Card(
        modifier = Modifier
            .graphicsLayer {
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
            .size(300.dp, 150.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Some music picture
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.DarkGray)
                        .padding(0.dp, 15.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.padding(20.dp, 0.dp)) {
                            Text(text = "Music Title", color = Color.White)
                            Text(text = "Music Author", color = Color.White)
                        }
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .padding(20.dp, 0.dp)
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = Icons.Filled.PlayArrow.name,
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongBottomCard() {
    Row(
        modifier = Modifier
            .padding(10.dp, 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(3.dp))
            .background(Color.LightGray)
            .padding(10.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color.DarkGray)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = "Music Title", color = Color.White, fontSize = 22.sp)
                Text(text = "Music Author", color = Color.White, fontSize = 18.sp)
            }
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = Icons.Filled.PlayArrow.name,
                modifier = Modifier.size(40.dp)
            )
        }

    }
}
