package com.example.products.ui.states

import com.example.products.domain.models.Product
import com.example.products.ui.errors.HomeError

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: HomeError? = null
)
