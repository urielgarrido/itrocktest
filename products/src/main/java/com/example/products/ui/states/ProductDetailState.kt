package com.example.products.ui.states

import com.example.products.domain.models.Product
import com.example.products.ui.errors.ProductDetailError

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: ProductDetailError? = null
)
