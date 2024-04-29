package com.uharris.mystore.domain.usecases

import app.cash.turbine.test
import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getCategories
import com.uharris.mystore.getProducts
import com.uharris.mystore.utils.AppResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCartUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: GetCartUseCase

    @Before
    fun setUp() {
        useCase = GetCartUseCase(storeRepository)
    }

    @Test
    fun `should get cart`() = runTest {
        coEvery { storeRepository.getCartProducts() } returns flow {
            emit(Cart(products = getProducts(), totalAmount = 195.00))
        }

        useCase.invoke(Unit).test {
            assertEquals(
                Cart(
                    products = getProducts(),
                    totalAmount = 195.00
                ),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { storeRepository.getCartProducts() }
    }
}