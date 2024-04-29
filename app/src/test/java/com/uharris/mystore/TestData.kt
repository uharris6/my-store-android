package com.uharris.mystore

import com.uharris.mystore.data.local.entity.CartProduct
import com.uharris.mystore.data.remote.dto.ProductDto
import com.uharris.mystore.data.remote.dto.RatingDto
import com.uharris.mystore.domain.model.Product

fun getProductsDTO() = listOf(
    getProductDTO(count = 400),
    getProductDTO(id = 2, title = "Pantalon verde")
)

fun getProductDTO(
    id: Long = 1,
    title: String = "Camisa roja",
    count: Int = 300
) = ProductDto(
    id = id,
    title = title,
    description = "Camisa roja de la mejor marca",
    price = 195.0,
    image = "image",
    rating = RatingDto(
        rate = 3.5,
        count = count
    )
)

fun getProducts() = listOf(
    getProduct(amount = 2),
    getProduct(id = 2, title = "Pantalon verde", amount = 1),
)

fun getProduct(
    id: Long = 1,
    title: String = "Camisa roja",
    amount: Int = 0
) = Product(
    id = id,
    title = title,
    description = "Camisa roja de la mejor marca",
    price = 195.0,
    image = "image",
    rate = 3.5,
    amountInCart = amount,
    isInCart = amount != 0
)

fun getCategories() = listOf(
    "jewelery",
    "electronics",
    "clothes",
    "shoes"
)

fun getCartProducts() = listOf(
    getCartProduct(amount = 2),
    getCartProduct(id = 2, title = "Pantalon verde", amount = 1)
)

fun getCartProduct(
    id: Long = 1,
    title: String = "Camisa roja",
    amount: Int = 0
) = CartProduct(
    id = id,
    title = title,
    price = 195.0,
    image = "image",
    amount = amount
)