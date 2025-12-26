package com.example.products.ui.viewmodels

import app.cash.turbine.test
import com.example.products.domain.models.Card
import com.example.products.domain.models.Product
import com.example.products.domain.usecases.BuyProductUseCase
import com.example.products.domain.usecases.GetProductDetailUseCase
import com.example.products.domain.usecases.ValidateCardUseCase
import com.example.products.domain.validation.CardValidationResult
import com.example.products.rules.MainDispatcherRule
import com.example.products.ui.errors.PaymentError
import com.example.products.ui.events.PaymentUIEvents
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PaymentViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var buyProductUseCase: BuyProductUseCase

    @RelaxedMockK
    private lateinit var getProductDetailUseCase: GetProductDetailUseCase

    @RelaxedMockK
    private lateinit var validateCardUseCase: ValidateCardUseCase

    private lateinit var viewModel: PaymentViewModel

    private val testProduct = Product(1, "Test Product", description = "Test Description", 100.0, "")

    @Before
    fun setUp() {
        viewModel = PaymentViewModel(
            buyProductUseCase,
            getProductDetailUseCase,
            validateCardUseCase
        )
    }

    @Test
    fun `updateCardNumber should update state and not exceed 16 digits`() = runTest {
        val longCardNumber = "1234567890123456789"
        val expectedCardNumber = "1234567890123456"

        viewModel.updateCardNumber(longCardNumber)

        val state = viewModel.paymentState.value
        assertThat(state.cardNumber).isEqualTo(expectedCardNumber)
    }

    @Test
    fun `updateCardHolder should update state`() = runTest {
        val cardHolder = "JOHN DOE"
        viewModel.updateCardHolder(cardHolder)
        assertThat(viewModel.paymentState.value.cardHolder).isEqualTo(cardHolder)
    }

    @Test
    fun `updateExpirationDate should update state and not exceed 4 digits`() = runTest {
        val longDate = "12259"
        val expectedDate = "1225"
        viewModel.updateExpirationDate(longDate)
        assertThat(viewModel.paymentState.value.expirationDate).isEqualTo(expectedDate)
    }

    @Test
    fun `updateCVV should update state and not exceed 3 digits`() = runTest {
        val longCvv = "1234"
        val expectedCvv = "123"
        viewModel.updateCVV(longCvv)
        assertThat(viewModel.paymentState.value.cvv).isEqualTo(expectedCvv)
    }

    @Test
    fun `checkFields should enable buy button when all fields are filled`() = runTest {
        assertThat(viewModel.paymentState.value.buyButtonEnabled).isFalse()

        viewModel.updateCardNumber("1111222233334444")
        viewModel.updateCardHolder("Jane Doe")
        viewModel.updateExpirationDate("1230")
        viewModel.updateCVV("123")

        assertThat(viewModel.paymentState.value.buyButtonEnabled).isTrue()
    }

    @Test
    fun `validateCard should call use case and update state with result`() = runTest {
        // Given
        val card = Card(
            cardNumber = "1111",
            cardHolder = "HOLDER",
            expirationDate = "1225",
            cvv = "123"
        )
        viewModel.updateCardNumber(card.cardNumber)
        viewModel.updateCardHolder(card.cardHolder)
        viewModel.updateExpirationDate(card.expirationDate)
        viewModel.updateCVV(card.cvv)

        every { validateCardUseCase(card) } returns CardValidationResult.Success

        // When
        viewModel.validateCard()

        // Then
        verify(exactly = 1) { validateCardUseCase(card) }
        assertThat(viewModel.paymentState.value.cardValidationResult).isEqualTo(CardValidationResult.Success)
    }

    @Test
    fun `validateCard should emit OnValidateCard event`() = runTest {
        viewModel.paymentUIEvents.test {
            awaitItem() // initial null state

            viewModel.validateCard()

            assertThat(awaitItem()).isInstanceOf(PaymentUIEvents.OnValidateCard::class.java)
        }
    }

    @Test
    fun `getProductDetail success should update state with product and stop loading`() = runTest {
        // Given
        coEvery { getProductDetailUseCase(1L) } returns flowOf(testProduct)

        viewModel.paymentState.test {
            awaitItem() // initial state

            // When
            viewModel.getProductDetail(1L)

            // Then
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.productToBuy).isEqualTo(testProduct)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `getProductDetail failure should update state with error`() = runTest {
        // Given
        coEvery { getProductDetailUseCase(1L) } returns flow { throw RuntimeException("Product not found") }

        viewModel.paymentState.test {
            awaitItem() // initial state
            // When
            viewModel.getProductDetail(1L)

            // Then
            awaitItem() // loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo(PaymentError.GetProductDetailError)
        }
    }

    @Test
    fun `buyProduct success should stop loading and emit OnProductBuySuccess event`() = runTest {
        // Given
        coEvery { getProductDetailUseCase(1L) } returns flowOf(testProduct)
        coEvery { buyProductUseCase(testProduct, "user1") } returns flowOf(Result.success(Unit))

        viewModel.getProductDetail(1L) // First, load the product into state

        viewModel.paymentUIEvents.test {
            awaitItem() // initial null event

            // When
            viewModel.buyProduct("user1")

            // Then
            assertThat(awaitItem()).isInstanceOf(PaymentUIEvents.OnProductBuySuccess::class.java)

            val finalState = viewModel.paymentState.value
            assertThat(finalState.isLoading).isFalse()
        }
    }

    @Test
    fun `buyProduct failure should stop loading and update state with error`() = runTest {
        // GIVEN: Preparamos todos los mocks necesarios.
        coEvery { getProductDetailUseCase(1L) } returns flowOf(testProduct)
        coEvery { buyProductUseCase(testProduct, "user1") } returns flow {
            emit(Result.failure(Exception()))
        }

        // Y establecemos el estado inicial del ViewModel cargando el producto.
        // Esta acción se completa ANTES de empezar a observar con Turbine.
        viewModel.getProductDetail(1L)

        // THEN: Ahora sí, iniciamos el colector de Turbine para observar los eventos de la acción que nos interesa.
        viewModel.paymentState.test {
            // 1. Lo primero que `awaitItem()` recibe es el estado MÁS RECIENTE,
            // que es el resultado de `getProductDetail` (producto cargado, isLoading = false).
            // Consumimos este estado para asegurar que partimos de la situación correcta.
            val initialState = awaitItem()
            assertThat(initialState.productToBuy).isEqualTo(testProduct)
            assertThat(initialState.isLoading).isFalse()

            // WHEN: Dentro del colector, ejecutamos la acción a probar.
            viewModel.buyProduct("user1")

            // 2. Ahora esperamos la primera emisión provocada por `buyProduct`, que debe ser el estado de carga.
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // 3. Y finalmente, esperamos la segunda emisión, que es el estado de error.
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo(PaymentError.BuyProductError)

            // Nos aseguramos de que no haya más emisiones inesperadas.
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetPaymentUIEvents should set event to null`() = runTest {
        viewModel.paymentUIEvents.test {
            awaitItem() // initial null

            viewModel.onValidateCardEvent()
            assertThat(awaitItem()).isNotNull() // event emitted

            viewModel.resetPaymentUIEvents()
            assertThat(awaitItem()).isNull() // event is reset
        }
    }
}