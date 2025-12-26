package com.example.products.ui.viewmodels

import app.cash.turbine.test
import com.example.products.domain.models.Product
import com.example.products.domain.usecases.GetProductDetailUseCase
import com.example.products.rules.MainDispatcherRule
import com.example.products.ui.errors.ProductDetailError
import com.example.products.ui.events.ProductDetailUIEvents
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getProductDetailUseCase: GetProductDetailUseCase

    private lateinit var viewModel: ProductDetailViewModel

    private val testProductId = 1L
    private val testProduct = Product(
        id = testProductId,
        name = "Test Product",
        description = "A great product",
        price = 99.99,
        imageUrl = "http://example.com/image.png"
    )

    @Before
    fun setUp() {
        viewModel = ProductDetailViewModel(getProductDetailUseCase)
    }

    @Test
    fun `when getProductDetail is successful, state should transition from loading to success`() = runTest {
        // Given
        coEvery { getProductDetailUseCase(testProductId) } returns flowOf(testProduct)

        // When & Then
        viewModel.productDetailState.test {
            // Estado inicial por defecto
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.product).isNull()

            // Acción
            viewModel.getProductDetail(testProductId)

            // Estado de carga
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // Estado final con el producto
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.product).isEqualTo(testProduct)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `when getProductDetail fails, state should transition from loading to error`() = runTest {
        // Given
        val exception = RuntimeException("Product not found")
        coEvery { getProductDetailUseCase(testProductId) } returns flow { throw exception }

        // When & Then
        viewModel.productDetailState.test {
            // Consumimos el estado inicial
            awaitItem()

            // Acción
            viewModel.getProductDetail(testProductId)

            // Estado de carga
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // Estado final con el error
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.product).isNull()
            assertThat(errorState.error).isEqualTo(ProductDetailError.GetProductDetailError)
        }
    }

    @Test
    fun `when onProductWant is called with a loaded product, it should emit OnProductWant event`() = runTest {
        // Given: Primero, cargamos el producto en el estado.
        coEvery { getProductDetailUseCase(testProductId) } returns flowOf(testProduct)    // Esta acción se ejecuta y completa antes de que empecemos el test del evento.
        viewModel.getProductDetail(testProductId)

        // When & Then
        viewModel.productDetailUIEvents.test {
            // StateFlow siempre emite su valor actual al empezar a colectar.
            assertThat(awaitItem()).isNull()

            // 2. Ahora sí, ejecutamos la acción que va a disparar el nuevo evento.
            viewModel.onProductWant()

            // 3. Verificamos que el nuevo evento fue recibido correctamente.
            val event = awaitItem()
            assertThat(event).isInstanceOf(ProductDetailUIEvents.OnProductWant::class.java)
            assertThat((event as ProductDetailUIEvents.OnProductWant).product).isEqualTo(testProduct)

            expectNoEvents()
        }
    }

    @Test
    fun `when onProductWant is called without a loaded product, it should not emit any event`() = runTest {
        // Given: El estado está inicial, sin producto.

        // When & Then
        viewModel.productDetailUIEvents.test {
            // Consumimos el estado inicial (null)
            awaitItem()

            // Acción
            viewModel.onProductWant()

            // Verificamos que no se emitió ningún evento nuevo.
            expectNoEvents()
        }
    }

    @Test
    fun `when resetProductDetailUIEvents is called, the event should be null`() = runTest {
        // Given
        coEvery { getProductDetailUseCase(testProductId) } returns flowOf(testProduct)
        viewModel.getProductDetail(testProductId)

        viewModel.productDetailUIEvents.test {
            // 1. Consumimos el estado inicial nulo
            assertThat(awaitItem()).isNull()

            // 2. Disparamos un evento
            viewModel.onProductWant()
            // 3. Consumimos y verificamos que se emitió el evento
            assertThat(awaitItem()).isNotNull()

            // When
            viewModel.resetProductDetailUIEvents()

            // Then
            // 4. Verificamos que el evento se ha reseteado a null
            assertThat(awaitItem()).isNull()
        }
    }
}
