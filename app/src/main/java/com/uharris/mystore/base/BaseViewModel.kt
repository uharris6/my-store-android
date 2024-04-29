package com.uharris.mystore.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<S : State, in E : Event> : ViewModel() {
    abstract val state: Flow<S>

    abstract fun updateState(newState: (S) -> S)
}