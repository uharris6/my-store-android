package com.uharris.mystore.presentation.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.uharris.mystore.MainDispatcherRule
import com.uharris.mystore.domain.usecases.AddProductToCartUseCase
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetProductDetailUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.getProduct
import com.uharris.mystore.navigation.RouteNavigator
import com.uharris.mystore.presentation.products.ProductsEvent
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
class ProductDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductDetail: GetProductDetailUseCase = mockk(relaxed = true)
    private val addProductToCart: AddProductToCartUseCase = mockk(relaxed = true)
    private val updateProductFromCart: UpdateProductFromCartUseCase = mockk(relaxed = true)
    private val deleteProductFromCart: DeleteProductFromCartUseCase = mockk(relaxed = true)
    private val routeNavigator: RouteNavigator = mockk(relaxed = true)

    private fun createViewModel() = ProductDetailViewModel(
        getProductDetail = getProductDetail,
        addProductToCart = addProductToCart,
        updateProductFromCart = updateProductFromCart,
        deleteProductFromCart = deleteProductFromCart,
        routeNavigator = routeNavigator,
        ioDispatcher = UnconfinedTestDispatcher(),
        mainDispatcher = UnconfinedTestDispatcher(),
        savedStateHandle = SavedStateHandle(mapOf(PRODUCT_DETAIL_ID_PARAM to "1"))
    )

    @Test
    fun `on create view model, should update state with product`() = runTest {
        coEvery { getProductDetail.invoke(any()) } returns flow { emit(getProduct()) }

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                ProductDetailState(
                    isLoading = false,
                    product = getProduct()
                )
            )
        }
    }

    @Test
    fun `on event back click, should navigate to products`() = runTest {
        val viewModel = createViewModel()

        viewModel.sendEvent(ProductDetailEvent.OnBackClick)

        coVerify { routeNavigator.navigateUp() }
    }

    @Test
    fun `on event add product click, should add product to cart`() = runTest {
        coEvery { getProductDetail.invoke(any()) } returns flow { emit(getProduct()) }

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals(
                awaitItem(),
                ProductDetailState(
                    isLoading = false,
                    product = getProduct()
                )
            )
        }

        viewModel.sendEvent(ProductDetailEvent.OnAddProductClick)

        coVerify { addProductToCart(any()) }
    }

    @Test
    fun `on event decrease product click, should decrease amount product in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(ProductDetailEvent.OnDecreaseAmountInCart(getProduct(amount = 1)))

        coVerify { updateProductFromCart(any()) }
    }

    @Test
    fun `on event decrease product click, should delete product in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(ProductDetailEvent.OnDecreaseAmountInCart(getProduct()))

        coVerify { deleteProductFromCart(any()) }
    }

    @Test
    fun `on event increase product click, should increase product amount in cart`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.sendEvent(ProductDetailEvent.OnIncreaseAmountInCart(getProduct()))

        coVerify { updateProductFromCart(any()) }
    }
}