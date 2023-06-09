package com.volokhinaleksey.musicapp.app

import android.app.Application
import com.volokhinaleksey.musicapp.di.dataSources
import com.volokhinaleksey.musicapp.di.database
import com.volokhinaleksey.musicapp.di.datastore
import com.volokhinaleksey.musicapp.di.exoPlayer
import com.volokhinaleksey.musicapp.di.homeScreen
import com.volokhinaleksey.musicapp.di.repositories
import com.volokhinaleksey.musicapp.di.songScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicApplication)
            modules(
                listOf(
                    database,
                    datastore,
                    repositories,
                    dataSources,
                    homeScreen,
                    exoPlayer,
                    songScreen
                )
            )
        }
    }

}