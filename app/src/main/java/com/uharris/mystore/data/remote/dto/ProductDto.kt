package com.uharris.mystore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("rating")
    val rating: RatingDto
)

data class RatingDto(
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("count")
    val count: Int
)
