package com.volokhinaleksey.musicapp.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.volokhinaleksey.core.exoplayer.MusicNotificationManager
import com.volokhinaleksey.core.exoplayer.MusicService
import com.volokhinaleksey.core.exoplayer.MusicServiceConnection
import com.volokhinaleksey.core.utils.MUSIC_DATABASE_NAME
import com.volokhinaleksey.core.utils.MUSIC_PREFERENCES
import com.volokhinaleksey.database.MusicDatabase
import com.volokhinaleksey.datasource.description.DescriptionDataSource
import com.volokhinaleksey.datasource.description.DescriptionLocalDataSource
import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.datasource.home.HomeLocalDataSource
import com.volokhinaleksey.datasource.main.MainDataSource
import com.volokhinaleksey.datasource.main.MainLocalDataSourceImpl
import com.volokhinaleksey.description_screen.viewmodel.DescriptionMusicViewModel
import com.volokhinaleksey.home_screen.viewmodel.HomeScreenViewModel
import com.volokhinaleksey.interactors.description.DescriptionInteractor
import com.volokhinaleksey.interactors.description.DescriptionInteractorImpl
import com.volokhinaleksey.interactors.home.HomeInteractor
import com.volokhinaleksey.interactors.home.HomeInteractorImpl
import com.volokhinaleksey.interactors.main.MainInteractor
import com.volokhinaleksey.interactors.main.MainInteractorImpl
import com.volokhinaleksey.repositories.description.DescriptionRepository
import com.volokhinaleksey.repositories.description.DescriptionRepositoryImpl
import com.volokhinaleksey.repositories.home.HomeRepository
import com.volokhinaleksey.repositories.home.HomeRepositoryImpl
import com.volokhinaleksey.repositories.main.MainRepository
import com.volokhinaleksey.repositories.main.MainRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val database = module {
    single {
        Room
            .databaseBuilder(get(), MusicDatabase::class.java, MUSIC_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val dataSources = module {
    single<MainDataSource> { MainLocalDataSourceImpl(get(), get()) }
    single<HomeDataSource> { HomeLocalDataSource(get()) }
    single<DescriptionDataSource> { DescriptionLocalDataSource(get()) }
}

val datastore = module {
    single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single {
        val appContext = androidContext()
        PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
            migrations = listOf(SharedPreferencesMigration(appContext, MUSIC_PREFERENCES)),
            scope = get(),
            produceFile = { appContext.preferencesDataStoreFile(name = MUSIC_PREFERENCES) }
        )
    }
}

val repositories = module {
    single<MainRepository> { MainRepositoryImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<DescriptionRepository> { DescriptionRepositoryImpl(get()) }
}

val homeScreen = module {
    factory { Dispatchers.IO }
    single { MusicServiceConnection(get()) }
    factory<MainInteractor> { MainInteractorImpl(get()) }
    factory<HomeInteractor> { HomeInteractorImpl(get(), get()) }
    viewModel { HomeScreenViewModel(get(), get(), get(), get()) }
}

val songScreen = module {
    factory { Dispatchers.IO }
    single { MusicServiceConnection(get()) }
    factory<DescriptionInteractor> { DescriptionInteractorImpl(get(), get()) }
    viewModel { DescriptionMusicViewModel(get(), get(), get(), get()) }
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