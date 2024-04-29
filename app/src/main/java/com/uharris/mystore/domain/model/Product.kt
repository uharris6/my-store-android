package com.uharris.mystore.domain.model

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String = "",
    val image: String,
    val rate: Double = 0.0,
    val amountInCart: Int = 0,
    val isInCart: Boolean = false
)
