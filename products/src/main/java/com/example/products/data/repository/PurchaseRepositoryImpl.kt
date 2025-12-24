package com.example.products.data.repository

import com.example.products.domain.dataSource.PurchaseRemoteDataSource
import com.example.products.domain.models.Product
import com.example.products.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseRemoteDataSource: PurchaseRemoteDataSource
) : PurchaseRepository {

    override suspend fun buyProduct(product: Product, userUID: String) = purchaseRemoteDataSource.savePurchase(product, userUID)

    override suspend fun purchaseHistory(userUID: String) = purchaseRemoteDataSource.getPurchaseHistory(userUID)
}