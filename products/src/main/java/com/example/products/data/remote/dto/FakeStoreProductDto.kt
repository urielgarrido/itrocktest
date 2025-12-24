package com.example.products.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FakeStoreProductDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val imageUrl: String
)
