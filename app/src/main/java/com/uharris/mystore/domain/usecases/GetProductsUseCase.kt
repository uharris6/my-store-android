package com.uharris.mystore.domain.usecases

import com.uharris.mystore.base.FlowUseCase
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.domain.model.Store
import com.uharris.mystore.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) : FlowUseCase<String, Store>() {
    override fun run(params: String): Flow<Store> = storeRepository.getProducts(params)
}