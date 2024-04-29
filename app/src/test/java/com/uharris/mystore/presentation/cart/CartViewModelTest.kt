package com.uharris.mystore.presentation.cart

import app.cash.turbine.test
import com.uharris.mystore.MainDispatcherRule
import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetCartUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.getProduct
import com.uharris.mystore.getProducts
import com.uharris.mystore.navigation.RouteNavigator
import com.uharris.mystore.presentation.detail.ProductDetailState
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
class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCart: GetCartUseCase = mockk(relaxed = true)
    private val updateProductFromCart: UpdateProductFromCartUseCase = mockk(relaxed = true)
    private val deleteProductFromCart: DeleteProductFromCartUseCase = mockk(relaxed = true)
    private val routeNavigator: RouteNavigator = mockk(relaxed = true)

    private fun createViewModel() = CartViewModel(
        getCart = getCart,
        updateProductFromCart = updateProductFromCart,
        deleteProductFromCart = deleteProductFromCart,
        routeNavigator = routeNavigator,
        ioDispatcher = UnconfinedTestDispatcher(),
        mainDispatcher = UnconfinedTestDispatcher(),
    )

    @Test
    fun `on create view model, should update state with product`() = runTest {
        coEvery { getCart.invoke(Unit) } returns flow {
            emit(
                Cart(
                    products = getProducts(),
                    totalAmount = 400.0
                )
            )
        }

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                CartState(
                    cartProducts = getProducts(),
                    totalAmount = 400.0
                )
            )
        }
    }

    @Test
    fun `on event back click, should navigate to products`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(CartEvent.OnBackClick)

        coVerify { routeNavigator.navigateUp() }
    }

    @Test
    fun `on event decrease product click, should decrease amount product in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(CartEvent.OnDecreaseAmountInCart(getProduct(amount = 1)))

        coVerify { updateProductFromCart(any()) }
    }

    @Test
    fun `on event decrease product click, should delete product in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(CartEvent.OnDecreaseAmountInCart(getProduct()))

        coVerify { deleteProductFromCart(any()) }
    }

    @Test
    fun `on event increase product click, should increase product amount in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(CartEvent.OnIncreaseAmountInCart(getProduct()))

        coVerify { updateProductFromCart(any()) }
    }

    @Test
    fun `on event delete product click, should delete product in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(CartEvent.OnDeleteProductClick(getProduct()))

        coVerify { deleteProductFromCart(any()) }
    }
}