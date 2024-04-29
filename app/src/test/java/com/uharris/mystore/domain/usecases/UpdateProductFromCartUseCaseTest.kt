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

class UpdateProductFromCartUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: UpdateProductFromCartUseCase

    @Before
    fun setUp() {
        useCase = UpdateProductFromCartUseCase(storeRepository)
    }

    @Test
    fun `should update Product from Cart`() = runTest {
        coEvery { storeRepository.updateProductFromCart(any()) } just Runs

        useCase.invoke(getProduct())

        coVerify { storeRepository.updateProductFromCart(any()) }
    }
}