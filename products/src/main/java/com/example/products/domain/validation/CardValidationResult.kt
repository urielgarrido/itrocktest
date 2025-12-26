package com.example.products.domain.validation

sealed class CardValidationResult {
    object Success : CardValidationResult()
    object Error : CardValidationResult()
}
