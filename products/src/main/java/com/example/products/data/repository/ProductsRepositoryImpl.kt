package com.example.products.data.repository

import com.example.core.domain.dataSource.UserPreferences
import com.example.products.domain.dataSource.ProductsDataSourceFactory
import com.example.products.domain.models.Product
import com.example.products.domain.repository.ProductsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductsRepositoryImpl @Inject constructor(
    private val factory: ProductsDataSourceFactory,
    private val userPreferences: UserPreferences
) : ProductsRepository {

    override suspend fun getProducts(): Flow<List<Product>> {
        val country = userPreferences.getSelectedCountry().first()
        return factory.create(country).getProducts()
    }

    override suspend fun getProductDetail(id: Long): Flow<Product> {
        val country = userPreferences.getSelectedCountry().first()
        return factory.create(country).getProductDetail(id)
    }
}