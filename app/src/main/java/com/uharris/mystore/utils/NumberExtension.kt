package com.uharris.mystore.utils

import java.text.NumberFormat
import java.util.Currency

fun Int?.orZero() = this ?: 0

fun Double.toCurrency(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.isGroupingUsed = false
    format.currency = Currency.getInstance("CLP")

    return format.format(this)
}