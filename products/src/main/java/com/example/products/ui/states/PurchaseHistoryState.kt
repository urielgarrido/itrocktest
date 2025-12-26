package com.example.products.ui.states

import com.example.products.domain.models.Purchase
import com.example.products.ui.errors.PurchaseHistoryError

data class PurchaseHistoryState(
    val purchases: List<Purchase>? = null,
    val isLoading: Boolean = false,
    val error: PurchaseHistoryError? = null
)
