package com.volokhinaleksey.repositories

import com.volokhinaleksey.models.local.Track

interface HomeRepository {

    fun getSongs(): List<Track>

}