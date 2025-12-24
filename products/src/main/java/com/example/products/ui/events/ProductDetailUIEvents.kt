package com.example.products.ui.events

import com.example.products.domain.models.Product

sealed interface ProductDetailUIEvents {
    data class OnProductWant(val product: Product) : ProductDetailUIEvents
    data class OnProductBuy(val product: Product) : ProductDetailUIEvents
}