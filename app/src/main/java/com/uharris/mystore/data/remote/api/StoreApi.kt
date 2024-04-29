package com.uharris.mystore.data.remote.api

import com.uharris.mystore.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    @GET(PRODUCTS_URL)
    suspend fun getProducts(): List<ProductDto>

    @GET("$PRODUCTS_URL/category/{$CATEGORY_PATH}")
    suspend fun getProductsByCategory(@Path(CATEGORY_PATH) category: String): List<ProductDto>

    @GET("$PRODUCTS_URL/{$PRODUCT_ID_PATH}")
    suspend fun getProductDetail(@Path(PRODUCT_ID_PATH) productId: String): ProductDto

    @GET("$PRODUCTS_URL/$CATEGORIES_URL")
    suspend fun getCategories(): List<String>

    companion object {
        private const val PRODUCTS_URL = "products"
        private const val CATEGORIES_URL = "categories"
        private const val CATEGORY_PATH = "category"
        private const val PRODUCT_ID_PATH = "id"
    }
}