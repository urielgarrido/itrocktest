package com.example.products.ui.viewmodels

import app.cash.turbine.test
import com.example.products.domain.models.Product
import com.example.products.domain.usecases.GetAllProductsUseCase
import com.example.products.rules.MainDispatcherRule
import com.example.products.ui.errors.HomeError
import com.example.products.ui.events.HomeUIEvents
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

class HomeViewModelTest {

    // Regla para inicializar mocks de MockK
    @get:Rule
    val mockkRule = MockKRule(this)

    // Regla para reemplazar el Dispatcher.Main por uno de test
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getAllProductsUseCase: GetAllProductsUseCase

    private lateinit var viewModel: HomeViewModel

    // Datos de prueba
    private val productList = listOf(
        Product(id = 1, name = "Product 1", description = "Description 1", price = 10.0, imageUrl = ""),
        Product(id = 2, name = "Product 2", description = "Description 2", price = 20.0, imageUrl = "")
    )

    @Before
    fun setUp() {
        viewModel = HomeViewModel(getAllProductsUseCase)
    }

    @Test
    fun `when getAllProducts is called, state should transition to loading and then to success`() = runTest {
        // Given: Configuramos el caso de uso para que devuelva un flujo con la lista de productos.
        coEvery { getAllProductsUseCase() } returns flowOf(productList)

        // Then: Usamos turbine para observar los cambios en homeState.
        viewModel.homeState.test {
            // 1. El estado inicial tiene isLoading = false.
            var currentState = awaitItem()
            assertThat(currentState.isLoading).isFalse()

            // When: Llamamos a la función que queremos probar.
            viewModel.getAllProducts()

            // 2. El siguiente estado emitido debe ser con isLoading = true.
            currentState = awaitItem()
            assertThat(currentState.isLoading).isTrue()

            // 3. El estado final debe tener los productos y isLoading = false.
            currentState = awaitItem()
            assertThat(currentState.isLoading).isFalse()
            assertThat(currentState.products).isEqualTo(productList)
            assertThat(currentState.error).isNull()

            // Ya no esperamos más emisiones, podemos cancelar el colector de turbine.
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when getAllProducts succeeds, state should contain product list and not be loading`() = runTest {
        // Given
        coEvery { getAllProductsUseCase() } returns flowOf(productList)

        // When
        viewModel.getAllProducts()

        // Then
        viewModel.homeState.test {
            // El estado inicial es el de por defecto (isLoading = false)
            // Cuando llamamos a getAllProducts, se setea a isLoading=true
            // Luego el flow emite y se setea a isLoading=false y con la lista
            // Por lo tanto, el último estado debe ser el correcto.
            val finalState = awaitItem() // Consumimos el estado final tras la corrutina
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.products).isEqualTo(productList)
            assertThat(finalState.error).isNull()
        }
    }

    @Test
    fun `when getAllProducts fails, state should contain error and not be loading`() = runTest {
        // Given
        val exception = RuntimeException("Network Error")
        coEvery { getAllProductsUseCase() } returns flow { throw exception }

        // When
        viewModel.getAllProducts()

        // Then
        viewModel.homeState.test {
            val finalState = awaitItem()
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.error).isInstanceOf(HomeError.GetProductsError::class.java)
            assertThat(finalState.products).isEmpty()
        }
    }

    @Test
    fun `when onProductClick is called, it should emit OnProductClicked event`() = runTest {
        val productId = 123L

        viewModel.homeUIEvents.test {
            // Estado inicial es null
            assertThat(awaitItem()).isNull()

            // When
            viewModel.onProductClick(productId)

            // Then
            val event = awaitItem()
            assertThat(event).isInstanceOf(HomeUIEvents.OnProductClicked::class.java)
            assertThat((event as HomeUIEvents.OnProductClicked).id).isEqualTo(productId)
        }
    }

    @Test
    fun `when resetHomeUIEvents is called, the event should be null`() = runTest {
        val productId = 123L

        viewModel.homeUIEvents.test {
            // Consumimos el estado inicial nulo
            awaitItem()

            // Disparamos un evento
            viewModel.onProductClick(productId)
            // Consumimos el evento
            assertThat(awaitItem()).isNotNull()

            // When
            viewModel.resetHomeUIEvents()

            // Then
            // Verificamos que el evento se ha reseteado a null
            assertThat(awaitItem()).isNull()
        }
    }
}
