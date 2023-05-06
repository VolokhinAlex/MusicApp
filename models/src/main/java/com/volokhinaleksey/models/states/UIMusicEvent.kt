package com.volokhinaleksey.models.states

sealed class UIMusicEvent {
    object PlayPause : UIMusicEvent()
    object Prev : UIMusicEvent()
    object Next : UIMusicEvent()
    data class UpdateProgress(val newProgress: Float) : UIMusicEvent()
    data class RepeatMode(val mode: Int) : UIMusicEvent()
}