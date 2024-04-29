package com.uharris.mystore.domain.repository

import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.utils.AppResult
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getProducts(category: String): Flow<Store>

    suspend fun getCategories(): AppResult<List<String>>

    fun getProductDetail(id: String): Flow<Product>

    suspend fun addProductToCart(product: Product)

    suspend fun deleteProductFromCart(product: Product)

    suspend fun updateProductFromCart(product: Product)

    fun getCartProducts(): Flow<Cart>
}