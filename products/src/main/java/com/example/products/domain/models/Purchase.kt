package com.example.products.domain.models

data class Purchase(
    val id: String,
    val productId: Long,
    val productName: String,
    val purchaseState: PurchaseState,
    val purchaseDate: Long = System.currentTimeMillis()
)

enum class PurchaseState {
    COMPLETED
}
