package com.uharris.mystore.domain.mapper

import com.uharris.mystore.data.local.entity.CartProduct
import com.uharris.mystore.data.remote.dto.ProductDto
import com.uharris.mystore.domain.model.Product

fun ProductDto.toDomain(
    amount: Int = 0
) = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    image = image,
    rate = rating.rate,
    amountInCart = amount,
    isInCart = amount != 0
)

fun Product.toCartProduct(amount: Int? = null) = CartProduct(
    id = id,
    title = title,
    price = price,
    image = image,
    amount = amount ?: this.amountInCart
)

fun CartProduct.toDomain() = Product(
    id = id,
    title = title,
    price = price,
    image = image,
    amountInCart = amount,
    isInCart = amount != 0
)