package com.uharris.mystore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartProduct(
    @PrimaryKey
    val id: Long,
    val title: String,
    val price: Double,
    val image: String,
    val amount: Int
)
