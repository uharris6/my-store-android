package com.uharris.mystore.domain.model

data class Cart(
    val products: List<Product>,
    val totalAmount: Double,
)
