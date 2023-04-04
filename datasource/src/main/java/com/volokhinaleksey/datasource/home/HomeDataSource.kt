package com.volokhinaleksey.datasource.home

import com.volokhinaleksey.models.local.Track

interface HomeDataSource {

    fun getSongs() : List<Track>

}