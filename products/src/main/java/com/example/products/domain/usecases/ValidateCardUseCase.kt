package com.example.products.domain.usecases

import com.example.products.domain.models.Card
import com.example.products.domain.validation.CardValidationResult
import com.example.products.domain.validation.CardValidator
import javax.inject.Inject

class ValidateCardUseCase @Inject constructor(
    private val cardValidator: CardValidator
) {
    operator fun invoke(card: Card): CardValidationResult = cardValidator.validate(card)
}