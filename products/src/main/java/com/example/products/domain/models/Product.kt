package com.example.products.domain.models

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String

)
