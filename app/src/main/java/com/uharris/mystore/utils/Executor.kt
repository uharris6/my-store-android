package com.uharris.mystore.utils

interface Executor {
    suspend fun <T> launchInNetworkThread(block: suspend () -> T) : T
}