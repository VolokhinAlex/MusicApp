package com.volokhinaleksey.models.states

sealed class UIEvent {
    object PlayPause : UIEvent()
    object Prev : UIEvent()
    object Next : UIEvent()
    object Shuffle : UIEvent()
    data class UpdateProgress(val newProgress: Float) : UIEvent()
    data class RepeatMode(val mode: Int) : UIEvent()
}