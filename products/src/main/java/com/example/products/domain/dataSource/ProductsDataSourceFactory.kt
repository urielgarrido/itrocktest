package com.example.products.domain.dataSource

import com.example.core.domain.models.Country

interface ProductsDataSourceFactory {
    fun create(country: Country): ProductsDataSource
}
