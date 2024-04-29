package com.uharris.mystore.presentation.products

import com.uharris.mystore.base.Event
import com.uharris.mystore.base.State
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.presentation.cart.CartEvent

data class ProductsState(
    val isLoading: Boolean,
    val products: List<Product>,
    val category: String,
    val amountInCart: Int,
) : State {
    companion object {
        fun initial() = ProductsState(
            isLoading = true,
            products = emptyList(),
            category = "",
            amountInCart = 0
        )
    }
}

sealed class ProductsEvent : Event {
    data object OnCartClick : ProductsEvent()
    data class OnProductClick(val id: Long): ProductsEvent()
    data class OnAddProductClick(val product: Product): ProductsEvent()
    data class OnDecreaseAmountInCart(val product: Product): ProductsEvent()
    data class OnIncreaseAmountInCart(val product: Product): ProductsEvent()
}