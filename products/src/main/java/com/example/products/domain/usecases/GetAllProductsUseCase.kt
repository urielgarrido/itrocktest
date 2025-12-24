package com.example.products.domain.usecases

import com.example.products.domain.models.Product
import com.example.products.domain.repository.ProductsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(): Flow<List<Product>> = repository.getProducts()
}