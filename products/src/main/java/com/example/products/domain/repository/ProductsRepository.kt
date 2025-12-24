package com.example.products.domain.repository

import com.example.products.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun getProductDetail(id: Long): Flow<Product>
}