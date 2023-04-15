package com.volokhinaleksey.musicapp.app

import android.app.Application
import com.volokhinaleksey.musicapp.di.dataSources
import com.volokhinaleksey.musicapp.di.homeScreen
import com.volokhinaleksey.musicapp.di.repositories
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicApplication)
            modules(listOf(repositories, dataSources, homeScreen))
        }
    }

}