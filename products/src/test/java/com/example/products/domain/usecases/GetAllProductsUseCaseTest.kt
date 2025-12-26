package com.example.products.domain.usecases

import app.cash.turbine.test
import com.example.products.domain.models.Product
import com.example.products.domain.repository.ProductsRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetAllProductsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockRepository: ProductsRepository

    private lateinit var getAllProductsUseCase: GetAllProductsUseCase

    @Before
    fun setUp() {
        getAllProductsUseCase = GetAllProductsUseCase(mockRepository)
    }

    @Test
    fun `when use case is invoked then repository's getProducts should be called`() = runTest {
        // Given
        // Configuramos el mock para que devuelva un flujo vacío,
        // ya que solo queremos verificar la llamada.
        coEvery { mockRepository.getProducts() } returns flowOf()

        // When
        getAllProductsUseCase()

        // Then
        // Verificamos que el método getProducts del repositorio fue llamado exactamente una vez.
        coVerify(exactly = 1) { mockRepository.getProducts() }
    }

    @Test
    fun `when repository returns products then it should emit a list of products`() = runTest {
        // Given
        val productList = listOf(
            Product(id = 1, name = "Laptop", description = "A good laptop", price = 1200.0, imageUrl = ""),
            Product(id = 2, name = "Mouse", description = "A good mouse", price = 25.0, imageUrl = "")
        )
        // Configuramos el mock para que devuelva un flujo con la lista de productos.
        coEvery { mockRepository.getProducts() } returns flowOf(productList)

        // When
        val resultFlow = getAllProductsUseCase()

        // Then
        // Usamos turbine para verificar las emisiones del flujo.
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea la lista de productos esperada.
            val emission = awaitItem()
            assertThat(emission).isEqualTo(productList)
            assertThat(emission.size).isEqualTo(2)
            // Verificamos que no haya más emisiones y que el flujo se complete.
            awaitComplete()
        }
    }

    @Test
    fun `when repository returns an empty list then it should emit an empty list`() = runTest {
        // Given
        // Configuramos el mock para que devuelva un flujo con una lista vacía.
        coEvery { mockRepository.getProducts() } returns flowOf(emptyList())

        // When
        val resultFlow = getAllProductsUseCase()

        // Then
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea una lista vacía.
            val emission = awaitItem()
            assertThat(emission).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `when repository throws an exception then the flow should emit an error`() = runTest {
        // Given
        val exception = RuntimeException("Network Error")
        // Configuramos el mock para que devuelva un Flow que emite un error.
        coEvery { mockRepository.getProducts() } returns flow {
            throw exception
        }

        // When
        val resultFlow = getAllProductsUseCase()

        // Then
        resultFlow.test {
            // Verificamos que el flujo termina con un error y que la excepción es la que esperamos.
            val error = awaitError()
            assertThat(error).isInstanceOf(RuntimeException::class.java)
            assertThat(error).isEqualTo(exception)
        }
    }
}
