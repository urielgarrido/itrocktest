package com.example.products.domain.usecases

import com.example.products.domain.models.Card
import com.example.products.domain.validation.CardValidationResult
import com.example.products.domain.validation.CardValidator
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateCardUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockCardValidator: CardValidator

    private lateinit var validateCardUseCase: ValidateCardUseCase

    // Datos de tarjeta válidos para los tests
    private val validCard = Card(
        cardNumber = "1111222233334444",
        cardHolder = "URIEL GARRIDO",
        expirationDate = "0930",
        cvv = "123"
    )

    @Before
    fun setUp() {
        validateCardUseCase = ValidateCardUseCase(mockCardValidator)
    }

    @Test
    fun `when use case is invoked then validator's validate should be called`() {
        // Given
        val card = validCard

        // When
        validateCardUseCase(card)

        // Then
        // Verificamos que el método validate del validador fue llamado exactamente una vez con la tarjeta correcta.
        verify(exactly = 1) { mockCardValidator.validate(card) }
    }

    @Test
    fun `given a valid card when validator returns Success then use case should return Success`() {
        // Given
        val card = validCard
        // Configuramos el mock para que devuelva un resultado exitoso.
        every { mockCardValidator.validate(card) } returns CardValidationResult.Success

        // When
        val result = validateCardUseCase(card)

        // Then
        // Verificamos que el resultado del caso de uso es Success.
        assertThat(result).isInstanceOf(CardValidationResult.Success::class.java)
    }

    @Test
    fun `given an invalid card when validator returns Error then use case should return Error`() {
        // Given
        val invalidCard = validCard.copy(cvv = "000") // Tarjeta con datos incorrectos
        // Configuramos el mock para que devuelva un resultado de error.
        every { mockCardValidator.validate(invalidCard) } returns CardValidationResult.Error

        // When
        val result = validateCardUseCase(invalidCard)

        // Then
        // Verificamos que el resultado del caso de uso es Error.
        assertThat(result).isInstanceOf(CardValidationResult.Error::class.java)
    }
}