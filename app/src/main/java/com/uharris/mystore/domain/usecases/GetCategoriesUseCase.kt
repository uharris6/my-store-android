package com.uharris.mystore.domain.usecases

import com.uharris.mystore.base.UseCase
import com.uharris.mystore.domain.repository.StoreRepository
import com.uharris.mystore.utils.AppResult
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) : UseCase<Unit, AppResult<List<String>>>() {
    override suspend fun run(params: Unit): AppResult<List<String>> =
        storeRepository.getCategories()

}