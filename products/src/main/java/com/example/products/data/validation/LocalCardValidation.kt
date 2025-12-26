package com.example.products.data.validation

import com.example.products.domain.models.Card
import com.example.products.domain.validation.CardValidationResult
import com.example.products.domain.validation.CardValidator
import javax.inject.Inject

class LocalCardValidation @Inject constructor(): CardValidator {
    override fun validate(card: Card): CardValidationResult {
        return when {
            card.cardNumber != "1111222233334444" ->
                CardValidationResult.Error

            card.cardHolder.uppercase() != "URIEL GARRIDO" ->
                CardValidationResult.Error

            card.expirationDate != "0930" ->
                CardValidationResult.Error

            card.cvv != "123" ->
                CardValidationResult.Error

            else -> CardValidationResult.Success
        }
    }
}