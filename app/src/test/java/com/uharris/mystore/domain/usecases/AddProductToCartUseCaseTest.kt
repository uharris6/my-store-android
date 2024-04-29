package com.uharris.mystore.domain.usecases

import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getProduct
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddProductToCartUseCaseTest {

    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: AddProductToCartUseCase

    @Before
    fun setUp() {
        useCase = AddProductToCartUseCase(storeRepository)
    }

    @Test
    fun `should add Product to Cart`() = runTest {
        coEvery { storeRepository.addProductToCart(any()) } just Runs

        useCase.invoke(getProduct())

        coVerify { storeRepository.addProductToCart(any()) }
    }
}