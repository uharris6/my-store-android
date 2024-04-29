package com.uharris.mystore.presentation.cart

import com.uharris.mystore.base.Event
import com.uharris.mystore.base.State
import com.uharris.mystore.domain.model.Product

data class CartState(
    val cartProducts: List<Product>,
    val totalAmount: Double,
): State {
    companion object {
        fun initial() = CartState(
            cartProducts = emptyList(),
            totalAmount = 0.0
        )
    }
}

sealed class CartEvent: Event {
    data object OnBackClick: CartEvent()
    data class OnDeleteProductClick(val product: Product): CartEvent()
    data class OnDecreaseAmountInCart(val product: Product): CartEvent()
    data class OnIncreaseAmountInCart(val product: Product): CartEvent()
}