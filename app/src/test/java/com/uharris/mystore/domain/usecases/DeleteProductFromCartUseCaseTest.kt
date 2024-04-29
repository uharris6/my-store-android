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

class DeleteProductFromCartUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: DeleteProductFromCartUseCase

    @Before
    fun setUp() {
        useCase = DeleteProductFromCartUseCase(storeRepository)
    }

    @Test
    fun `should delete Product from Cart`() = runTest {
        coEvery { storeRepository.deleteProductFromCart(any()) } just Runs

        useCase.invoke(getProduct())

        coVerify { storeRepository.deleteProductFromCart(any()) }
    }
}