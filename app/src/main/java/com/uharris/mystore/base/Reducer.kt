package com.uharris.mystore.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class Reducer<S: State, E: Event>(initialValue: S) {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)
    val state: StateFlow<S>
        get() = _state

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }

    init {
        timeCapsule.addState(initialValue)
    }

    fun sendEvent(event: E) {
        reduce(_state.value, event)
    }

    fun setState(newState: S) {
        val success = _state.tryEmit(newState)

        if (success) {
            timeCapsule.addState(newState)
        }
    }

    abstract fun reduce(oldState: S, event: E)
}

interface State
interface Event