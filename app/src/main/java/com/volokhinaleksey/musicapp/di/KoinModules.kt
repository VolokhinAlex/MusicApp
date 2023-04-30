package com.volokhinaleksey.musicapp.di

import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.core.exoplayer.MusicNotificationManager
import com.volokhinaleksey.core.exoplayer.MusicService
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.datasource.home.HomeLocalDataSourceImpl
import com.volokhinaleksey.description_screen.viewmodel.DescriptionMusicViewModel
import com.volokhinaleksey.home_screen.viewmodel.HomeScreenViewModel
import com.volokhinaleksey.interactors.home.HomeInteractor
import com.volokhinaleksey.interactors.home.HomeInteractorImpl
import com.volokhinaleksey.models.states.TrackState
import com.volokhinaleksey.repositories.HomeRepository
import com.volokhinaleksey.repositories.HomeRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataSources = module {
    single<HomeDataSource> { HomeLocalDataSourceImpl(get()) }
}

val repositories = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
}

val homeScreen = module {
    factory<HomeInteractor> { HomeInteractorImpl(get()) }
    viewModel<BaseViewModel<TrackState>> { HomeScreenViewModel(get()) }
}

val songScreen = module {
    factory { MusicServiceConnection(get()) }
    viewModel { DescriptionMusicViewModel(get(), get()) }
}

@UnstableApi
val exoPlayer = module {
    single<Player> {
        ExoPlayer.Builder(get()).setTrackSelector(DefaultTrackSelector(get())).build()
    }
    single { MediaSession.Builder(get(), get()).build() }
    single { MusicNotificationManager(get(), get()) }
    single { MusicService() }
}