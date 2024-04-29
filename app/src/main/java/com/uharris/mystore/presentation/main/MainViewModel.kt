package com.uharris.mystore.presentation.main

import androidx.lifecycle.viewModelScope
import com.uharris.mystore.base.BaseViewModel
import com.uharris.mystore.base.Reducer
import com.uharris.mystore.domain.usecases.GetCategoriesUseCase
import com.uharris.mystore.utils.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase
) : BaseViewModel<MainState, MainEvent>() {
    override val state: StateFlow<MainState>
        get() = reducer.state

    override fun updateState(newState: (MainState) -> MainState) {
        reducer.setState(newState.invoke(reducer.state.value))
    }

    private val reducer = MainReducer(MainState.initial())

    fun sendEvent(event: MainEvent) {
        reducer.sendEvent(event)
    }

    inner class MainReducer(initialValue: MainState) :
        Reducer<MainState, MainEvent>(initialValue) {
        override fun reduce(oldState: MainState, event: MainEvent) {
            when (event) {
                is MainEvent.OnCategoryClick -> setState(
                    oldState.copy(categorySelected = event.category)
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            when (val categories = getCategories(Unit)) {
                is AppResult.Success -> updateState {
                    it.copy(
                        categories = categories.data,
                    )
                }

                is AppResult.Error -> Unit
            }
        }
    }
}