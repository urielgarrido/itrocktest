package com.example.products.ui.errors

sealed class PurchaseHistoryError {
    object GetPurchaseHistoryError : PurchaseHistoryError()
}