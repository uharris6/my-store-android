package com.uharris.mystore.domain.usecases

import com.uharris.mystore.base.UseCase
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.domain.repository.StoreRepository
import javax.inject.Inject

class DeleteProductFromCartUseCase @Inject constructor(
    private val storeRepository: StoreRepository
): UseCase<Product, Unit>() {
    override suspend fun run(params: Product) {
        storeRepository.deleteProductFromCart(params)
    }
}