package com.uharris.mystore.presentation.products

import androidx.lifecycle.viewModelScope
import com.uharris.mystore.base.BaseViewModel
import com.uharris.mystore.base.Reducer
import com.uharris.mystore.di.IoDispatcher
import com.uharris.mystore.di.MainImmediateDispatcher
import com.uharris.mystore.domain.usecases.AddProductToCartUseCase
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetProductsUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.navigation.RouteNavigator
import com.uharris.mystore.presentation.cart.CartEvent
import com.uharris.mystore.presentation.cart.CartRoute
import com.uharris.mystore.presentation.detail.navigateProductDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val addProductToCart: AddProductToCartUseCase,
    private val updateProductFromCart: UpdateProductFromCartUseCase,
    private val deleteProductFromCart: DeleteProductFromCartUseCase,
    private val routeNavigator: RouteNavigator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<ProductsState, ProductsEvent>(), RouteNavigator by routeNavigator {
    private var job: Job? = null

    override val state: StateFlow<ProductsState>
        get() = reducer.state

    override fun updateState(newState: (ProductsState) -> ProductsState) {
        reducer.setState(newState.invoke(reducer.state.value))
    }

    private val reducer = ProductsReducer(ProductsState.initial())

    fun sendEvent(event: ProductsEvent) {
        reducer.sendEvent(event)
    }

    inner class ProductsReducer(initialValue: ProductsState) :
        Reducer<ProductsState, ProductsEvent>(initialValue) {
        override fun reduce(oldState: ProductsState, event: ProductsEvent) {
            when (event) {
                is ProductsEvent.OnProductClick ->
                    routeNavigator.navigateToRoute(navigateProductDetail(event.id))

                is ProductsEvent.OnAddProductClick -> {
                    viewModelScope.launch(mainDispatcher) {
                        addProductToCart(event.product)
                    }
                }
                is ProductsEvent.OnCartClick -> routeNavigator.navigateToRoute(CartRoute.route)
                is ProductsEvent.OnDecreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        if (event.product.amountInCart > 0) {
                            updateProductFromCart(event.product)
                        } else {
                            deleteProductFromCart(event.product)
                        }
                    }
                }
                is ProductsEvent.OnIncreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        updateProductFromCart(event.product)
                    }
                }
            }
        }
    }

    fun onLoad(category: String) {
        job?.cancel()
        job = viewModelScope.launch(ioDispatcher) {
            getProducts(category)
                .catch { }.collect { store ->
                    viewModelScope.launch(mainDispatcher) {
                        updateState { oldState ->
                            oldState.copy(
                                isLoading = false,
                                products = store.products,
                                category = category,
                                amountInCart = store.amountInCart
                            )
                        }
                    }
                }
        }
    }
}