package com.example.products.data.validation

import com.example.products.domain.models.Card
import com.example.products.domain.validation.CardValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LocalCardValidationTest {

    private lateinit var validator: LocalCardValidation

    // Datos base para una tarjeta válida
    private val validCard = Card(
        cardNumber = "1111222233334444",
        cardHolder = "URIEL GARRIDO",
        expirationDate = "0930",
        cvv = "123"
    )

    @Before
    fun setUp() {
        validator = LocalCardValidation()
    }

    @Test
    fun `when card number is incorrect, validate returns Error`() {
        // Given: Una tarjeta con el número incorrecto
        val card = validCard.copy(cardNumber = "0000000000000000")

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Error
        assertThat(result).isEqualTo(CardValidationResult.Error)
    }

    @Test
    fun `when card holder is incorrect, validate returns Error`() {
        // Given: Una tarjeta con el titular incorrecto
        val card = validCard.copy(cardHolder = "JOHN DOE")

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Error
        assertThat(result).isEqualTo(CardValidationResult.Error)
    }

    @Test
    fun `when card holder has different case but is correct, validate returns Success`() {
        // Given: Una tarjeta con el titular en minúsculas
        val card = validCard.copy(cardHolder = "uriel garrido")

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Success, ya que la lógica usa .uppercase()
        assertThat(result).isEqualTo(CardValidationResult.Success)
    }

    @Test
    fun `when expiration date is incorrect, validate returns Error`() {
        // Given: Una tarjeta con la fecha de expiración incorrecta
        val card = validCard.copy(expirationDate = "1225")

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Error
        assertThat(result).isEqualTo(CardValidationResult.Error)
    }

    @Test
    fun `when cvv is incorrect, validate returns Error`() {
        // Given: Una tarjeta con el CVV incorrecto
        val card = validCard.copy(cvv = "999")

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Error
        assertThat(result).isEqualTo(CardValidationResult.Error)
    }

    @Test
    fun `when all card details are correct, validate returns Success`() {
        // Given: La tarjeta con todos los datos válidos
        val card = validCard

        // When: Se valida la tarjeta
        val result = validator.validate(card)

        // Then: El resultado debe ser Success
        assertThat(result).isEqualTo(CardValidationResult.Success)
    }
}