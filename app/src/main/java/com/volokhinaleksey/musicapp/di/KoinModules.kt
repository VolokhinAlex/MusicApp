package com.volokhinaleksey.musicapp.di

import com.volokhinaleksey.core.base.BaseViewModel
import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.datasource.home.HomeLocalDataSourceImpl
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