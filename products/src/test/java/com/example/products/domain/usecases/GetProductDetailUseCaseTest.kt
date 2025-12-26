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

class GetProductDetailUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockRepository: ProductsRepository

    private lateinit var getProductDetailUseCase: GetProductDetailUseCase

    @Before
    fun setUp() {
        getProductDetailUseCase = GetProductDetailUseCase(mockRepository)
    }

    @Test
    fun `when use case is invoked then repository's getProductDetail should be called`() = runTest {
        // Given
        val productId = 1L
        // Configuramos el mock para que devuelva un flujo, ya que solo queremos verificar la llamada.
        coEvery { mockRepository.getProductDetail(any()) } returns flowOf()

        // When
        getProductDetailUseCase(productId)

        // Then
        // Verificamos que el método getProductDetail del repositorio fue llamado exactamente una vez con el ID correcto.
        coVerify(exactly = 1) { mockRepository.getProductDetail(productId) }
    }

    @Test
    fun `when repository returns a product then it should emit the product`() = runTest {
        // Given
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            description = "A great product",
            price = 99.99,
            imageUrl = "http://example.com/image.png"
        )
        // Configuramos el mock para que devuelva un flujo con el producto.
        coEvery { mockRepository.getProductDetail(productId) } returns flowOf(product)

        // When
        val resultFlow = getProductDetailUseCase(productId)

        // Then
        // Usamos turbine para verificar la emisión del flujo.
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea el producto esperado.
            val emission = awaitItem()
            assertThat(emission).isEqualTo(product)
            // Verificamos que no haya más emisiones y que el flujo se complete.
            awaitComplete()
        }
    }

    @Test
    fun `when repository throws an exception then the flow should emit an error`() = runTest {
        // Given
        val productId = 2L
        val exception = RuntimeException("Product not found")
        // Configuramos el mock para que devuelva un Flow que emite un error.
        coEvery { mockRepository.getProductDetail(productId) } returns flow {
            throw exception
        }

        // When
        val resultFlow = getProductDetailUseCase(productId)

        // Then
        resultFlow.test {
            // Verificamos que el flujo termina con un error y que la excepción es la que esperamos.
            val error = awaitError()
            assertThat(error).isInstanceOf(RuntimeException::class.java)
            assertThat(error.message).isEqualTo("Product not found")
        }
    }
}