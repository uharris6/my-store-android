package com.uharris.mystore.presentation.main

import app.cash.turbine.test
import com.uharris.mystore.MainDispatcherRule
import com.uharris.mystore.domain.usecases.GetCategoriesUseCase
import com.uharris.mystore.utils.AppResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCategories: GetCategoriesUseCase = mockk(relaxed = true)

    @Test
    fun `onCreate view model should get categories and update state`() = runTest {
        coEvery { getCategories.invoke(Unit) } returns AppResult.Success(
            listOf(
                "jewelery",
                "electronics",
                "clothes",
                "shoes"
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                MainState(
                    categories = listOf(
                        "jewelery",
                        "electronics",
                        "clothes",
                        "shoes"
                    ),
                    categorySelected = "todos"
                )
            )
        }
    }

    @Test
    fun `when select category, then state will update`() = runTest {
        coEvery { getCategories.invoke(Unit) } returns AppResult.Success(
            listOf(
                "jewelery",
                "electronics",
                "clothes",
                "shoes"
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                MainState(
                    categories = listOf(
                        "jewelery",
                        "electronics",
                        "clothes",
                        "shoes"
                    ),
                    categorySelected = "todos"
                )
            )
        }

        viewModel.sendEvent(MainEvent.OnCategoryClick("jewelery"))

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                MainState(
                    categories = listOf(
                        "jewelery",
                        "electronics",
                        "clothes",
                        "shoes"
                    ),
                    categorySelected = "jewelery"
                )
            )
        }

    }

    private fun createViewModel() = MainViewModel(getCategories)
}