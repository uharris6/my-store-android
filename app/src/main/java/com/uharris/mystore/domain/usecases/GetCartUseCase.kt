package com.uharris.mystore.domain.usecases

import com.uharris.mystore.base.FlowUseCase
import com.uharris.mystore.domain.model.Cart
import com.uharris.mystore.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) : FlowUseCase<Unit, Cart>() {
    override fun run(params: Unit): Flow<Cart> = storeRepository.getCartProducts()
}