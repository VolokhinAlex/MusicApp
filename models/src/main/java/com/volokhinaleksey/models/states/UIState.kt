package com.volokhinaleksey.models.states

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}