package com.uharris.mystore.presentation.detail

import com.uharris.mystore.base.Event
import com.uharris.mystore.base.State
import com.uharris.mystore.domain.model.Product

data class ProductDetailState(
    val isLoading: Boolean,
    val product: Product?,
): State {
    companion object {
        fun initial() = ProductDetailState(
            isLoading = true,
            product = null
        )
    }
}

sealed class ProductDetailEvent: Event {
    data object OnBackClick: ProductDetailEvent()
    data object OnAddProductClick: ProductDetailEvent()
    data class OnDecreaseAmountInCart(val product: Product): ProductDetailEvent()
    data class OnIncreaseAmountInCart(val product: Product): ProductDetailEvent()
}