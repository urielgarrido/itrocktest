package com.example.products.ui.errors

sealed class PaymentError {
    object BuyProductError : PaymentError()
    object GetProductDetailError : PaymentError()
}