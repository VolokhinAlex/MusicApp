package com.volokhinaleksey.models.states

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object Prev : PlayerEvent()
    object Next : PlayerEvent()
    object Stop : PlayerEvent()
    object Shuffle : PlayerEvent()
    data class RepeatMode(val mode: Int) : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}