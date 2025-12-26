package com.example.products.data.repository

import app.cash.turbine.test
import com.example.core.domain.dataSource.UserPreferences
import com.example.core.domain.models.Country
import com.example.products.domain.dataSource.ProductsDataSource
import com.example.products.domain.dataSource.ProductsDataSourceFactory
import com.example.products.domain.models.Product
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockFactory: ProductsDataSourceFactory

    @RelaxedMockK
    private lateinit var mockUserPreferences: UserPreferences

    @RelaxedMockK
    private lateinit var mockDataSourceA: ProductsDataSource

    @RelaxedMockK
    private lateinit var mockDataSourceB: ProductsDataSource

    private lateinit var repository: ProductsRepositoryImpl

    @Before
    fun setUp() {
        // Esta parte es correcta
        coEvery { mockFactory.create(Country.A) } returns mockDataSourceA
        coEvery { mockFactory.create(Country.B) } returns mockDataSourceB

        repository = ProductsRepositoryImpl(mockFactory, mockUserPreferences)
    }

    @Test
    fun `when getProducts and country is A, then it should use DataSource A`() = runTest {
        // Given
        coEvery { mockUserPreferences.getSelectedCountry() } returns flowOf(Country.A)
        val productList = listOf(Product(1, "Product from A", "", 10.0, ""))
        coEvery { mockDataSourceA.getProducts() } returns flowOf(productList)

        // When
        val resultFlow = repository.getProducts()

        // Then
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(productList)
            awaitComplete()
        }


        verify(exactly = 1) { mockFactory.create(Country.A) }
        coVerify(exactly = 1) { mockDataSourceA.getProducts() }
        coVerify(exactly = 0) { mockDataSourceB.getProducts() }
    }

    @Test
    fun `when getProducts and country is B, then it should use DataSource B`() = runTest {
        // Given
        coEvery { mockUserPreferences.getSelectedCountry() } returns flowOf(Country.B)
        val productList = listOf(Product(2, "Product from B", "", 20.0, ""))
        coEvery { mockDataSourceB.getProducts() } returns flowOf(productList)

        // When
        val resultFlow = repository.getProducts()

        // Then
        resultFlow.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(productList)
            awaitComplete()
        }

        verify(exactly = 1) { mockFactory.create(Country.B) }
        coVerify(exactly = 1) { mockDataSourceB.getProducts() }
        coVerify(exactly = 0) { mockDataSourceA.getProducts() }
    }

    @Test
    fun `when getProductDetail and country is A, then it should use DataSource A`() = runTest {
        // Given
        val productId = 1L
        coEvery { mockUserPreferences.getSelectedCountry() } returns flowOf(Country.A)
        val product = Product(productId, "Detail from A", "", 15.0, "")
        coEvery { mockDataSourceA.getProductDetail(productId) } returns flowOf(product)

        // When
        val resultFlow = repository.getProductDetail(productId)

        // Then
        resultFlow.test {
            assertThat(awaitItem()).isEqualTo(product)
            awaitComplete()
        }

        verify(exactly = 1) { mockFactory.create(Country.A) }
        coVerify(exactly = 1) { mockDataSourceA.getProductDetail(productId) }
        coVerify(exactly = 0) { mockDataSourceB.getProductDetail(any()) }
    }

    @Test
    fun `when getProductDetail and country is B, then it should use DataSource B`() = runTest {
        // Given
        val productId = 2L
        coEvery { mockUserPreferences.getSelectedCountry() } returns flowOf(Country.B)
        val product = Product(productId, "Detail from B", "", 25.0, "")
        coEvery { mockDataSourceB.getProductDetail(productId) } returns flowOf(product)

        // When
        val resultFlow = repository.getProductDetail(productId)

        // Then
        resultFlow.test {
            assertThat(awaitItem()).isEqualTo(product)
            awaitComplete()
        }

        verify(exactly = 1) { mockFactory.create(Country.B) }
        coVerify(exactly = 1) { mockDataSourceB.getProductDetail(productId) }
        coVerify(exactly = 0) { mockDataSourceA.getProductDetail(any()) }
    }
}
