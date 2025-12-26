package com.example.products.domain.usecases

import app.cash.turbine.test
import com.example.products.domain.models.Product
import com.example.products.domain.repository.PurchaseRepository
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

class BuyProductUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var purchaseRepository: PurchaseRepository

    private lateinit var buyProductUseCase: BuyProductUseCase

    @Before
    fun setUp() {
        buyProductUseCase = BuyProductUseCase(purchaseRepository)
    }

    @Test
    fun `when use case is invoked then repository's buyProduct should be called`() = runTest {
        // Given
        val product = Product(id = 1, name = "Test Product", description = "Test Description", price = 10.0, imageUrl = "")
        val userUID = "testUser"
        // Preparamos el mock para que devuelva un flujo vacío para evitar NullPointerException,
        // ya que solo queremos verificar la llamada.
        coEvery { purchaseRepository.buyProduct(any(), any()) } returns flowOf()


        // When
        buyProductUseCase(product, userUID)

        // Then
        // Verificamos que el método buyProduct del repositorio fue llamado exactamente una vez con los parámetros correctos.
        coVerify(exactly = 1) { purchaseRepository.buyProduct(product, userUID) }
    }

    @Test
    fun `when buyProduct is successful then it should emit Result success`() = runTest {
        // Given
        val product = Product(id = 1, name = "Test Product", description = "Test Description", price = 10.0, imageUrl = "")
        val userUID = "testUser"
        // Configuramos el mock para que devuelva un flujo con un resultado exitoso.
        coEvery { purchaseRepository.buyProduct(product, userUID) } returns flow {
            emit(Result.success(Unit))
        }

        // When
        val resultFlow = buyProductUseCase(product, userUID)

        // Then
        // Usamos turbine para verificar las emisiones del flujo.
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea un resultado exitoso.
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            // Verificamos que no haya más emisiones y que el flujo se complete.
            awaitComplete()
        }
    }

    @Test
    fun `when buyProduct fails then it should emit Result failure`() = runTest {
        // Given
        val product = Product(id = 1, name = "Test Product", description = "Test Description", price = 10.0, imageUrl = "")
        val userUID = "testUser"
        val exception = Exception("Network Error")
        // Configuramos el mock para que devuelva un flujo con un resultado de error.
        coEvery { purchaseRepository.buyProduct(product, userUID) } returns flow {
            emit(Result.failure(exception))
        }

        // When
        val resultFlow = buyProductUseCase(product, userUID)

        // Then
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea un resultado de error.
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            // Verificamos que la excepción sea la que esperamos.
            assertThat(emission.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }
}
