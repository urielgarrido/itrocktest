package com.example.products.ui.events

sealed interface PaymentUIEvents {
    object OnProductBuySuccess: PaymentUIEvents
    object OnValidateCard: PaymentUIEvents
}