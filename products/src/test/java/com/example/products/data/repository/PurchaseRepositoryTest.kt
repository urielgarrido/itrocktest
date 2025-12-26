package com.example.products.data.repository

import app.cash.turbine.test
import com.example.products.domain.dataSource.PurchaseRemoteDataSource
import com.example.products.domain.models.Product
import com.example.products.domain.models.Purchase
import com.example.products.domain.models.PurchaseState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PurchaseRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockDataSource: PurchaseRemoteDataSource

    private lateinit var repository: PurchaseRepositoryImpl

    private val testProduct = Product(1L, "Test Product", "", 100.0, "")
    private val testUserUID = "testUser123"

    @Before
    fun setUp() {
        repository = PurchaseRepositoryImpl(mockDataSource)
    }

    @Test
    fun `when buyProduct is called, it should call savePurchase on the data source`() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado exitoso.
        coEvery { mockDataSource.savePurchase(any(), any()) } returns flowOf(Result.success(Unit))

        // When: Llamamos al método del repositorio.
        repository.buyProduct(testProduct, testUserUID)

        // Then: Verificamos que se llamó al método correcto en el DataSource con los parámetros correctos.
        coVerify(exactly = 1) { mockDataSource.savePurchase(testProduct, testUserUID) }
    }

    @Test
    fun `when buyProduct is called, it should return the flow from the data source`() = runTest {
        // Given: Configuramos el mock para devolver un flujo específico.
        val expectedFlow = flowOf(Result.success(Unit))
        coEvery { mockDataSource.savePurchase(testProduct, testUserUID) } returns expectedFlow

        // When: Llamamos al método del repositorio.
        val resultFlow = repository.buyProduct(testProduct, testUserUID)

        // Then: Verificamos que el flujo devuelto es el mismo que el del DataSource.
        resultFlow.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `when purchaseHistory is called, it should call getPurchaseHistory on the data source`() = runTest {
        // Given: Configuramos el mock para que devuelva una lista vacía.
        coEvery { mockDataSource.getPurchaseHistory(any()) } returns flowOf(emptyList())

        // When: Llamamos al método del repositorio.
        repository.purchaseHistory(testUserUID)

        // Then: Verificamos que se llamó al método correcto en el DataSource.
        coVerify(exactly = 1) { mockDataSource.getPurchaseHistory(testUserUID) }
    }

    @Test
    fun `when purchaseHistory is called, it should return the purchase list from the data source`() = runTest {
        // Given: Configuramos el mock para que devuelva una lista de compras específica.
        val purchaseList = listOf(
            Purchase("1", 101L, "Product A", PurchaseState.COMPLETED),
            Purchase("2", 102L, "Product B", PurchaseState.COMPLETED)
        )
        coEvery { mockDataSource.getPurchaseHistory(testUserUID) } returns flowOf(purchaseList)

        // When: Llamamos al método del repositorio.
        val resultFlow = repository.purchaseHistory(testUserUID)

        // Then: Verificamos que el flujo emite la lista de compras esperada.
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(purchaseList)
            assertThat(emission).hasSize(2)
            awaitComplete()
        }
    }
}