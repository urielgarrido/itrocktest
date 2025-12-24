package com.example.products.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FakeStoreProductDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val imageUrl: String
)
