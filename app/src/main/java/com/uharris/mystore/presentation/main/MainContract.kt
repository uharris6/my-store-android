package com.uharris.mystore.presentation.main

import com.uharris.mystore.base.Event
import com.uharris.mystore.base.State

const val CATEGORY_ALL = "todos"

data class MainState(
    val categories: List<String>,
    val categorySelected: String,
): State {
    companion object {
        fun initial() = MainState(
            categories = emptyList(),
            categorySelected = CATEGORY_ALL,
        )
    }
}

sealed class MainEvent: Event {
    data class OnCategoryClick(val category: String): MainEvent()
}