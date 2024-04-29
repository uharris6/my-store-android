package com.uharris.mystore.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.uharris.mystore.base.BaseViewModel
import com.uharris.mystore.base.Reducer
import com.uharris.mystore.di.IoDispatcher
import com.uharris.mystore.di.MainImmediateDispatcher
import com.uharris.mystore.domain.usecases.AddProductToCartUseCase
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetProductDetailUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.navigation.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetail: GetProductDetailUseCase,
    private val addProductToCart: AddProductToCartUseCase,
    private val updateProductFromCart: UpdateProductFromCartUseCase,
    private val deleteProductFromCart: DeleteProductFromCartUseCase,
    private val routeNavigator: RouteNavigator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProductDetailState, ProductDetailEvent>(), RouteNavigator by routeNavigator {
    override val state: StateFlow<ProductDetailState>
        get() = reducer.state

    override fun updateState(newState: (ProductDetailState) -> ProductDetailState) {
        reducer.setState(newState.invoke(reducer.state.value))
    }

    private val reducer = ProductDetailReducer(ProductDetailState.initial())

    fun sendEvent(event: ProductDetailEvent) {
        reducer.sendEvent(event)
    }

    inner class ProductDetailReducer(initialValue: ProductDetailState) :
        Reducer<ProductDetailState, ProductDetailEvent>(initialValue) {
        override fun reduce(oldState: ProductDetailState, event: ProductDetailEvent) {
            when (event) {
                is ProductDetailEvent.OnBackClick -> routeNavigator.navigateUp()
                is ProductDetailEvent.OnAddProductClick -> {
                    viewModelScope.launch(mainDispatcher) {
                        oldState.product?.let {
                            addProductToCart(it)
                        }
                    }
                }
                is ProductDetailEvent.OnDecreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        if (event.product.amountInCart > 0) {
                            updateProductFromCart(event.product)
                        } else {
                            deleteProductFromCart(event.product)
                        }
                    }
                }
                is ProductDetailEvent.OnIncreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        updateProductFromCart(event.product)
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            getProductDetail(savedStateHandle[PRODUCT_DETAIL_ID_PARAM]!!)
                .catch { }
                .collect { product ->
                    viewModelScope.launch(mainDispatcher) {
                        updateState { oldState ->
                            oldState.copy(
                                isLoading = false,
                                product = product
                            )
                        }
                    }
                }
        }
    }
}