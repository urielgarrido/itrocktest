package com.example.products.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FakePlatziProductDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("images") val images: List<String>
)
