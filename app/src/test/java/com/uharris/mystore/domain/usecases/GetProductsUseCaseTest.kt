package com.uharris.mystore.domain.usecases

import app.cash.turbine.test
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getProducts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: GetProductsUseCase

    @Before
    fun setUp() {
        useCase = GetProductsUseCase(storeRepository)
    }

    @Test
    fun `should get products`() = runTest {
        coEvery { storeRepository.getProducts(any()) } returns flow {
            emit(Store(products = getProducts(), amountInCart = 3))
        }

        useCase.invoke("category").test {
            TestCase.assertEquals(
                Store(
                    products = getProducts(),
                    amountInCart = 3
                ),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { storeRepository.getProducts(any()) }
    }
}