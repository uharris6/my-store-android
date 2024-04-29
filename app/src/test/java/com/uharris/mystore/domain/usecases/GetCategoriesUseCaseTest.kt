package com.uharris.mystore.domain.usecases

import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getCategories
import com.uharris.mystore.getProduct
import com.uharris.mystore.utils.AppResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCategoriesUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        useCase = GetCategoriesUseCase(storeRepository)
    }

    @Test
    fun `should get categories`() = runTest {
        coEvery { storeRepository.getCategories() } returns AppResult.Success(getCategories())

        useCase.invoke(Unit)

        coVerify { storeRepository.getCategories() }
    }
}