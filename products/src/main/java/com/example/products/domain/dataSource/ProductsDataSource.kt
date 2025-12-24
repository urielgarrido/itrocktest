package com.example.products.domain.dataSource

import com.example.products.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductsDataSource {
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun getProductDetail(id: Long): Flow<Product>
}