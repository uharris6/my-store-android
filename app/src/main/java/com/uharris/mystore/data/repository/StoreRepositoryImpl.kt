package com.uharris.mystore.data.repository

import com.uharris.mystore.data.local.dao.CartProductDao
import com.uharris.mystore.data.remote.api.StoreApi
import com.uharris.mystore.domain.mapper.toCartProduct
import com.uharris.mystore.domain.mapper.toDomain
import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.presentation.main.CATEGORY_ALL
import com.uharris.mystore.utils.AppResult
import com.uharris.mystore.utils.Executor
import com.uharris.mystore.utils.orZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class StoreRepositoryImpl @Inject constructor(
    private val api: StoreApi,
    private val dao: CartProductDao,
    private val executor: Executor
) : StoreRepository {
    override fun getProducts(
        category: String
    ): Flow<Store> = dao.getAllCartProducts().flatMapLatest { cartProductList ->
        flow {
            try {
                emit(
                    Store(
                        if (category.lowercase() != CATEGORY_ALL) {
                            api.getProductsByCategory(category)
                        } else {
                            api.getProducts()
                        }.sortedByDescending {
                            it.rating.rate.times(it.rating.count)
                        }.map {
                            it.toDomain(
                                cartProductList.firstOrNull { cartProduct ->
                                    cartProduct.id == it.id
                                }?.amount.orZero()
                            )
                        },
                        amountInCart = cartProductList.sumOf { it.amount }
                    )
                )
            } catch (throwable: Throwable) {
                throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCategories(): AppResult<List<String>> = executor.launchInNetworkThread {
        try {
            AppResult.Success(api.getCategories())
        } catch (throwable: Throwable) {
            AppResult.Error(throwable)
        }
    }

    override fun getProductDetail(
        id: String
    ): Flow<Product> = dao.getCartProduct(id.toLong()).flatMapLatest {
        flow {
            try {
                emit(
                    api.getProductDetail(id)
                        .toDomain(it?.amount.orZero())
                )
            } catch (throwable: Throwable) {
                throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addProductToCart(product: Product) {
        executor.launchInNetworkThread {
            dao.insertCartProduct(product.toCartProduct(1))
        }
    }

    override suspend fun updateProductFromCart(product: Product) {
        executor.launchInNetworkThread {
            dao.updateCartProduct(product.toCartProduct())
        }
    }

    override suspend fun deleteProductFromCart(product: Product) {
        executor.launchInNetworkThread {
            dao.deleteCartProduct(product.toCartProduct())
        }
    }

    override fun getCartProducts(): Flow<Cart> = dao.getAllCartProducts().map { cartProductList ->
        Cart(
            products = cartProductList.map { it.toDomain() },
            totalAmount = cartProductList.sumOf { it.amount.times(it.price) }
        )
    }.flowOn(Dispatchers.IO)
}