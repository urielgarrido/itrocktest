package com.example.products.domain.dataSource

import com.example.products.domain.models.Product
import com.example.products.domain.models.Purchase
import kotlinx.coroutines.flow.Flow

interface PurchaseRemoteDataSource {
    suspend fun savePurchase(product: Product, userUID: String): Flow<Result<Unit>>
    suspend fun getPurchaseHistory(userUID: String): Flow<List<Purchase>>
}