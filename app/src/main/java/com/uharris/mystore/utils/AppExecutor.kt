package com.uharris.mystore.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AppExecutor(
    private val networkIO: CoroutineContext = Dispatchers.IO
) : Executor {
    override suspend fun <T> launchInNetworkThread(block: suspend () -> T) =
        withContext(networkIO) {
            block()
        }
}