package com.example.products.domain.usecases

import com.example.products.domain.repository.PurchaseRepository
import javax.inject.Inject

class GetPurchaseHistoryUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(userUID: String) = purchaseRepository.purchaseHistory(userUID)

}