package com.uharris.mystore.presentation.cart

import androidx.lifecycle.viewModelScope
import com.uharris.mystore.base.BaseViewModel
import com.uharris.mystore.base.Reducer
import com.uharris.mystore.di.IoDispatcher
import com.uharris.mystore.di.MainImmediateDispatcher
import com.uharris.mystore.domain.usecases.DeleteProductFromCartUseCase
import com.uharris.mystore.domain.usecases.GetCartUseCase
import com.uharris.mystore.domain.usecases.UpdateProductFromCartUseCase
import com.uharris.mystore.navigation.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCart: GetCartUseCase,
    private val updateProductFromCart: UpdateProductFromCartUseCase,
    private val deleteProductFromCart: DeleteProductFromCartUseCase,
    private val routeNavigator: RouteNavigator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<CartState, CartEvent>(), RouteNavigator by routeNavigator {
    override val state: StateFlow<CartState>
        get() = reducer.state

    override fun updateState(newState: (CartState) -> CartState) {
        reducer.setState(newState.invoke(reducer.state.value))
    }

    private val reducer = CartReducer(CartState.initial())

    fun sendEvent(event: CartEvent) {
        reducer.sendEvent(event)
    }

    inner class CartReducer(initialValue: CartState) :
        Reducer<CartState, CartEvent>(initialValue) {
        override fun reduce(oldState: CartState, event: CartEvent) {
            when (event) {
                is CartEvent.OnBackClick -> routeNavigator.navigateUp()
                is CartEvent.OnDeleteProductClick -> {
                    viewModelScope.launch(mainDispatcher) {
                        deleteProductFromCart(event.product)
                    }
                }
                is CartEvent.OnDecreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        if (event.product.amountInCart > 0) {
                            updateProductFromCart(event.product)
                        } else {
                            deleteProductFromCart(event.product)
                        }
                    }
                }
                is CartEvent.OnIncreaseAmountInCart -> {
                    viewModelScope.launch(mainDispatcher) {
                        updateProductFromCart(event.product)
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            getCart(Unit)
                .catch { }
                .collect { cart ->
                    viewModelScope.launch(mainDispatcher) {
                        updateState {
                            it.copy(
                                cartProducts = cart.products,
                                totalAmount = cart.totalAmount
                            )
                        }
                    }
                }
        }
    }
}