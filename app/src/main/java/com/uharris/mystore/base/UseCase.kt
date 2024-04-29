package com.uharris.mystore.base

abstract class UseCase<in Params, out T,> {
    abstract suspend fun run(params: Params): T

    suspend operator fun invoke(params: Params) : T  = run(params)
}