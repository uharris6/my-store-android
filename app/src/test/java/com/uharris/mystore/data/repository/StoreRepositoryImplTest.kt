package com.uharris.mystore.data.repository

import app.cash.turbine.test
import com.uharris.mystore.data.local.dao.CartProductDao
import com.uharris.mystore.data.remote.api.StoreApi
import com.uharris.mystore.domain.mapper.toDomain
import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.getCartProduct
import com.uharris.mystore.getCartProducts
import com.uharris.mystore.getCategories
import com.uharris.mystore.getProduct
import com.uharris.mystore.getProductDTO
import com.uharris.mystore.getProducts
import com.uharris.mystore.getProductsDTO
import com.uharris.mystore.utils.AppResult
import com.uharris.mystore.utils.TestExecutor
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StoreRepositoryImplTest {

    private val api: StoreApi = mockk(relaxed = true)
    private val dao: CartProductDao = mockk(relaxed = true)

    private lateinit var repository: StoreRepository

    @Before
    fun setUp() {
        repository = StoreRepositoryImpl(api, dao, TestExecutor())
    }

    @Test
    fun `should get categories, when api is get categories is called`() = runTest {
        coEvery { api.getCategories() } returns getCategories()

        val result = repository.getCategories()

        coVerify { api.getCategories() }
        assertEquals(true, result is AppResult.Success)
        assertEquals(getCategories().size, (result as AppResult.Success).data.size)
    }

    @Test
    fun `should add product, when dao add product to database`() = runTest {
        coEvery { dao.insertCartProduct(any()) } just Runs

        repository.addProductToCart(getProduct())

        coVerify { dao.insertCartProduct(any()) }
    }

    @Test
    fun `should update product, when dao update product to database`() = runTest {
        coEvery { dao.updateCartProduct(any()) } just Runs

        repository.updateProductFromCart(getProduct())

        coVerify { dao.updateCartProduct(any()) }
    }

    @Test
    fun `should delete product, when dao delete product to database`() = runTest {
        coEvery { dao.deleteCartProduct(any()) } just Runs

        repository.deleteProductFromCart(getProduct())

        coVerify { dao.deleteCartProduct(any()) }
    }

    @Test
    fun `should get product list flow, when api and dao get products`() = runTest {
        coEvery { api.getProducts() } returns getProductsDTO()
        coEvery { dao.getAllCartProducts() } returns flow { emit(getCartProducts()) }

        repository.getProducts("todos").test {
            assertEquals(
                Store(
                    products = getProducts(),
                    amountInCart = 3
                ),
                awaitItem()
            )
            awaitComplete()

        }

        coVerify { api.getProducts() }
        coVerify { dao.getAllCartProducts() }
    }

    @Test
    fun `should get products by category list flow, when api and dao get products`() = runTest {
        coEvery { api.getProductsByCategory(any()) } returns getProductsDTO()
        coEvery { dao.getAllCartProducts() } returns flow { emit(getCartProducts()) }

        repository.getProducts("jewelery").test {
            assertEquals(
                Store(
                    products = getProducts(),
                    amountInCart = 3
                ),
                awaitItem()
            )
            awaitComplete()

        }

        coVerify { api.getProductsByCategory(any()) }
        coVerify { dao.getAllCartProducts() }
    }

    @Test
    fun `should get cart products list flow, when dao get products`() = runTest {
        coEvery { dao.getAllCartProducts() } returns flow { emit(getCartProducts()) }

        repository.getCartProducts().test {
            assertEquals(
                Cart(
                    products = getCartProducts().map { it.toDomain() },
                    totalAmount = getProducts().sumOf { it.amountInCart.times(it.price) }
                ),
                awaitItem()
            )
            awaitComplete()

        }

        coVerify { dao.getAllCartProducts() }
    }

    @Test
    fun `should get product, when api and dao get specific product`() = runTest {
        coEvery { api.getProductDetail(any()) } returns getProductDTO()
        coEvery { dao.getCartProduct(any()) } returns flow { emit(getCartProduct(amount = 2)) }

        repository.getProductDetail("1").test {
            assertEquals(
                getProduct(amount = 2),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { api.getProductDetail(any()) }
        coVerify { dao.getCartProduct(any()) }
    }
}