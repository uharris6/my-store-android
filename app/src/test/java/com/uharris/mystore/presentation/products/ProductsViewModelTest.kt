package com.uharris.mystore.presentation.products

import app.cash.turbine.test
import com.uharris.mystore.MainDispatcherRule
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.usecases.AddProductToCartUseCase
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetProductsUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.getProduct
import com.uharris.mystore.getProducts
import com.uharris.mystore.navigation.RouteNavigator
import com.uharris.mystore.presentation.cart.CartRoute
import com.uharris.mystore.presentation.detail.navigateProductDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProducts: GetProductsUseCase = mockk(relaxed = true)
    private val addProductToCart: AddProductToCartUseCase = mockk(relaxed = true)
    private val updateProductFromCart: UpdateProductFromCartUseCase = mockk(relaxed = true)
    private val deleteProductFromCart: DeleteProductFromCartUseCase = mockk(relaxed = true)
    private val routeNavigator: RouteNavigator = mockk(relaxed = true)

    private fun createViewModel() = ProductsViewModel(
        getProducts = getProducts,
        addProductToCart = addProductToCart,
        updateProductFromCart = updateProductFromCart,
        deleteProductFromCart = deleteProductFromCart,
        routeNavigator = routeNavigator,
        ioDispatcher = UnconfinedTestDispatcher(),
        mainDispatcher = UnconfinedTestDispatcher()
    )

    @Test
    fun `onLoad view model, should update state with products`() = runTest {
        coEvery { getProducts.invoke(any()) } returns flow { emit(Store(getProducts(), 3)) }

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                ProductsState.initial()
            )
        }

        viewModel.onLoad("jewelery")
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                ProductsState(
                    isLoading = false,
                    products = getProducts(),
                    category = "jewelery",
                    amountInCart = 3
                )
            )
        }
    }

    @Test
    fun `on event product click, should navigate to product detail`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnProductClick(1L))

        coVerify { routeNavigator.navigateToRoute(navigateProductDetail(1)) }
    }

    @Test
    fun `on event cart click, should navigate to cart`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnCartClick)

        coVerify { routeNavigator.navigateToRoute(CartRoute.route) }
    }

    @Test
    fun `on event add product click, should add product to cart`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnAddProductClick(getProduct()))

        coVerify { addProductToCart(any()) }
    }

    @Test
    fun `on event decrease product click, should decrease amount product in cart`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnDecreaseAmountInCart(getProduct(amount = 1)))

        coVerify { updateProductFromCart(any()) }
    }

    @Test
    fun `on event decrease product click, should delete product in cart`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnDecreaseAmountInCart(getProduct()))

        coVerify { deleteProductFromCart(any()) }
    }

    @Test
    fun `on event increase product click, should increase product amount in cart`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductsEvent.OnIncreaseAmountInCart(getProduct()))

        coVerify { updateProductFromCart(any()) }
    }
}