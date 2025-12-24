package com.example.products.domain.usecases

import com.example.products.domain.models.Product
import com.example.products.domain.repository.PurchaseRepository
import javax.inject.Inject

class BuyProductUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {

    suspend operator fun invoke(product: Product, userUID: String) =
        purchaseRepository.buyProduct(product, userUID)
}