package com.example.products.domain.repository

import com.example.products.domain.models.Product
import com.example.products.domain.models.Purchase
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    suspend fun buyProduct(product: Product, userUID: String): Flow<Result<Unit>>
    suspend fun purchaseHistory(userUID: String): Flow<List<Purchase>>
}