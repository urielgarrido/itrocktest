package com.example.products.ui.events

sealed interface PaymentUIEvents {
    object OnProductBuy: PaymentUIEvents
}