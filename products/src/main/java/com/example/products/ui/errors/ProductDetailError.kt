package com.example.products.ui.errors

sealed class ProductDetailError {
    object GetProductDetailError : ProductDetailError()
}