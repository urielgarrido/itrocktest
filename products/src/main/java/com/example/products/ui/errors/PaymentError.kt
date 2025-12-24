package com.example.products.ui.errors

sealed class PaymentError {
    object EmptyCardNumber : PaymentError()
    object EmptyCardHolder : PaymentError()
    object EmptyExpirationDate : PaymentError()
    object EmptyCVV : PaymentError()
    object BuyProductError : PaymentError()
}