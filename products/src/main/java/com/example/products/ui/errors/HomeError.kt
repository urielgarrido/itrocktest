package com.example.products.ui.errors

sealed class HomeError {
    object GetProductsError : HomeError()
}