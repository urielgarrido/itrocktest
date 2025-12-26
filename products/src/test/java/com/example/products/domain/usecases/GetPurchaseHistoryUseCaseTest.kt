package com.example.products.domain.usecases

import app.cash.turbine.test
import com.example.products.domain.models.Purchase
import com.example.products.domain.models.PurchaseState
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

class GetPurchaseHistoryUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var purchaseRepository: PurchaseRepository

    private lateinit var getPurchaseHistoryUseCase: GetPurchaseHistoryUseCase

    @Before
    fun setUp() {
        getPurchaseHistoryUseCase = GetPurchaseHistoryUseCase(purchaseRepository)
    }

    @Test
    fun `when use case is invoked then repository's purchaseHistory should be called`() = runTest {
        // Given
        val userUID = "testUser"
        // Configuramos el mock para que devuelva un flujo vacío, ya que solo queremos verificar la llamada.
        coEvery { purchaseRepository.purchaseHistory(any()) } returns flowOf()

        // When
        getPurchaseHistoryUseCase(userUID)

        // Then
        // Verificamos que el método purchaseHistory del repositorio fue llamado exactamente una vez con el userUID correcto.
        coVerify(exactly = 1) { purchaseRepository.purchaseHistory(userUID) }
    }

    @Test
    fun `when purchaseHistory is successful then it should emit a list of purchases`() = runTest {
        // Given
        val userUID = "testUser"
        val purchaseList = listOf(
            Purchase(id = "1", productId = 101, productName = "Product A", purchaseState = PurchaseState.COMPLETED),
            Purchase(id = "2", productId = 102, productName = "Product B", purchaseState = PurchaseState.COMPLETED)
        )
        // Configuramos el mock para que devuelva un flujo con la lista de compras.
        coEvery { purchaseRepository.purchaseHistory(userUID) } returns flowOf(purchaseList)

        // When
        val resultFlow = getPurchaseHistoryUseCase(userUID)

        // Then
        // Usamos turbine para verificar las emisiones del flujo.
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea la lista de compras esperada.
            val emission = awaitItem()
            assertThat(emission).isEqualTo(purchaseList)
            // Verificamos que el tamaño de la lista emitida es correcto.
            assertThat(emission.size).isEqualTo(2)
            // Verificamos que no haya más emisiones y que el flujo se complete.
            awaitComplete()
        }
    }

    @Test
    fun `when purchaseHistory is successful but returns an empty list then it should emit an empty list`() = runTest {
        // Given
        val userUID = "testUserWithNoHistory"
        val emptyList = emptyList<Purchase>()
        // Configuramos el mock para que devuelva un flujo con una lista vacía.
        coEvery { purchaseRepository.purchaseHistory(userUID) } returns flowOf(emptyList)

        // When
        val resultFlow = getPurchaseHistoryUseCase(userUID)

        // Then
        resultFlow.test {
            // Esperamos la emisión y verificamos que sea una lista vacía.
            val emission = awaitItem()
            assertThat(emission).isEqualTo(emptyList)
            assertThat(emission.isEmpty()).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when purchaseHistory fails then the flow should emit an error`() = runTest { // Nombre del test ligeramente mejorado
        // Given
        val userUID = "testUser"
        val exception = RuntimeException("Database error")
        // Configuramos el mock para que devuelva un Flow que emite un error.
        coEvery { purchaseRepository.purchaseHistory(userUID) } returns flow {
            throw exception
        }

        // When
        val resultFlow = getPurchaseHistoryUseCase(userUID)

        // Then
        resultFlow.test {
            // Verificamos que el flujo termina con un error y que la excepción es la que esperamos.
            val error = awaitError()
            assertThat(error).isEqualTo(exception)
        }
    }
}
