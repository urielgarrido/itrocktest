package com.example.products.data.remote.api

import com.example.products.data.remote.dto.FakePlatziProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PlatziFakeStoreApi {

    @GET("products")
    suspend fun getProducts(): List<FakePlatziProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Long): FakePlatziProductDto
}