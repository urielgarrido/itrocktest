package com.example.products.data.dataSource

import com.example.core.domain.models.Country
import com.example.products.domain.dataSource.ProductsDataSource
import com.example.products.domain.dataSource.ProductsDataSourceFactory
import javax.inject.Inject

class ProductsDataSourceFactoryImpl @Inject constructor(
    private val countryADataSource: CountryAProductsDataSourceImpl,
    private val countryBDataSource: CountryBProductsDataSourceImpl
) : ProductsDataSourceFactory {

    override fun create(country: Country): ProductsDataSource {
        return when (country) {
            Country.A -> countryADataSource
            Country.B -> countryBDataSource
        }
    }
}
