package com.uharris.mystore.domain.usecases

import app.cash.turbine.test
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getProduct
import com.uharris.mystore.getProducts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProductDetailUseCaseTest {
    private val storeRepository: StoreRepository = mockk(relaxed = true)

    private lateinit var useCase: GetProductDetailUseCase

    @Before
    fun setUp() {
        useCase = GetProductDetailUseCase(storeRepository)
    }

    @Test
    fun `should get product detail`() = runTest {
        coEvery { storeRepository.getProductDetail(any()) } returns flow {
            emit(getProduct(amount = 2))
        }

        useCase.invoke("1").test {
            assertEquals(
                getProduct(amount = 2),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { storeRepository.getProductDetail(any()) }
    }
}