package com.example.products.domain.models

data class Card(
    val cardNumber: String,
    val cardHolder: String,
    val expirationDate: String,
    val cvv: String
)
