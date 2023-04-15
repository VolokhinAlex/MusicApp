package com.volokhinaleksey.repositories

import com.volokhinaleksey.datasource.home.HomeDataSource
import com.volokhinaleksey.models.local.Track

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override fun getSongs(): List<Track> = homeDataSource.getSongs()
}