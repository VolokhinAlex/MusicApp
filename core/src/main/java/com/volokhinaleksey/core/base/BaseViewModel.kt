package com.volokhinaleksey.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel<T : Any> : ViewModel() {

    protected val mutableData: MutableLiveData<T> = MutableLiveData()
    val data: LiveData<T> get() = mutableData

    protected val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}