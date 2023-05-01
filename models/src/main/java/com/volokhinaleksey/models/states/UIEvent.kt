package com.volokhinaleksey.models.states

sealed class UIEvent {
    object PlayPause : UIEvent()
    object Prev : UIEvent()
    object Next : UIEvent()
    data class UpdateProgress(val newProgress: Float) : UIEvent()
}