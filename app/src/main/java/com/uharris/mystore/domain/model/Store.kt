package com.uharris.mystore.domain.model

data class Store(
    val products: List<Product>,
    val amountInCart: Int,
)
