package com.example.products.ui.viewmodels

import app.cash.turbine.test
import com.example.products.domain.models.Purchase
import com.example.products.domain.models.PurchaseState
import com.example.products.domain.usecases.GetPurchaseHistoryUseCase
import com.example.products.rules.MainDispatcherRule
import com.example.products.ui.errors.PurchaseHistoryError
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

class PurchaseHistoryViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getPurchaseHistoryUseCase: GetPurchaseHistoryUseCase

    private lateinit var viewModel: PurchaseHistoryViewModel

    private val testUserUID = "testUser123"

    @Before
    fun setUp() {
        viewModel = PurchaseHistoryViewModel(getPurchaseHistoryUseCase)
    }

    @Test
    fun `when getPurchaseHistory is successful, state should transition from loading to success with purchases`() = runTest {
        // Given
        val purchaseList = listOf(
            Purchase("1", 101L, "Product A", PurchaseState.COMPLETED),
            Purchase("2", 102L, "Product B", PurchaseState.COMPLETED)
        )
        coEvery { getPurchaseHistoryUseCase(testUserUID) } returns flowOf(purchaseList)

        // When & Then
        viewModel.purchaseHistoryState.test {
            // Estado inicial
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.purchases).isNull()

            // Acción
            viewModel.getPurchaseHistory(testUserUID)

            // Estado de carga
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // Estado final con la lista de compras
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.purchases).isEqualTo(purchaseList)
            assertThat(successState.purchases?.size).isEqualTo(2)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `when getPurchaseHistory is successful but returns an empty list, state should show an empty list`() = runTest {
        // Given
        coEvery { getPurchaseHistoryUseCase(testUserUID) } returns flowOf(emptyList())

        // When & Then
        viewModel.purchaseHistoryState.test {
            // Consumimos el estado inicial
            awaitItem()

            // Acción
            viewModel.getPurchaseHistory(testUserUID)

            // Estado de carga
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // Estado final con la lista vacía
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.purchases).isNotNull()
            assertThat(successState.purchases).isEmpty()
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `when getPurchaseHistory fails, state should transition from loading to error`() = runTest {
        // Given
        val exception = RuntimeException("Firestore network error")
        coEvery { getPurchaseHistoryUseCase(testUserUID) } returns flow { throw exception }

        // When & Then
        viewModel.purchaseHistoryState.test {
            // Consumimos el estado inicial
            awaitItem()

            // Acción
            viewModel.getPurchaseHistory(testUserUID)

            // Estado de carga
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // Estado final con el error
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.purchases).isNull() // El estado de compras no se debe alterar
            assertThat(errorState.error).isEqualTo(PurchaseHistoryError.GetPurchaseHistoryError)
        }
    }
}