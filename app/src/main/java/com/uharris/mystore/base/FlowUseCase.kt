package com.uharris.mystore.base

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in Params, out T> {
    abstract fun run(params: Params): Flow<T>

    operator fun invoke(params: Params) : Flow<T>  = run(params)
}