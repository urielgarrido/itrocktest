package com.example.products.ui.states

import com.example.products.domain.models.Product
import com.example.products.ui.errors.PaymentError

data class PaymentState(
    val productToBuy: Product? = null,
    val cardNumber: String = "",
    val cardHolder: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val isLoading: Boolean = false,
    val error: PaymentError? = null,
    val buyButtonEnabled: Boolean = false
)
