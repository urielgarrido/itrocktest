package com.example.products.data.dataSource

import com.example.products.data.mappers.toDomain
import com.example.products.data.remote.api.PlatziFakeStoreApi
import com.example.products.domain.dataSource.ProductsDataSource
import com.example.products.domain.models.Product
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CountryBProductsDataSourceImpl @Inject constructor(
    private val api: PlatziFakeStoreApi
) : ProductsDataSource {

    override suspend fun getProducts(): Flow<List<Product>> = flow {
        runCatching {
            val response = api.getProducts()
            val products = response.map { it.toDomain() }
            emit(products)
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProductDetail(id: Long): Flow<Product> = flow {
        runCatching {
            val response = api.getProduct(id)
            val product = response.toDomain()
            emit(product)
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)
}
