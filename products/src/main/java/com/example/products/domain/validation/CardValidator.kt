package com.example.products.domain.validation

import com.example.products.domain.models.Card

interface CardValidator {
    fun validate(card: Card): CardValidationResult
}