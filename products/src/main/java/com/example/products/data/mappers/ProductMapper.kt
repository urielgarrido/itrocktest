package com.example.products.data.mappers

import com.example.products.data.remote.dto.FakePlatziProductDto
import com.example.products.data.remote.dto.FakeStoreProductDto
import com.example.products.domain.models.Product

fun FakeStoreProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        price = price,
        imageUrl = imageUrl
    )
}

fun FakePlatziProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        price = price,
        imageUrl = images.firstOrNull() ?: ""
    )
}