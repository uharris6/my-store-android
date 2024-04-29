package com.uharris.mystore.domain.usecases

import com.uharris.mystore.base.FlowUseCase
import com.uharris.mystore.base.UseCase
import com.uharris.mystore.domain.model.Product
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.utils.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) : FlowUseCase<String, Product?>() {
    override fun run(params: String): Flow<Product?> =
        storeRepository.getProductDetail(params)
}