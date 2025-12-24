package com.example.products.data.remote.api

import com.example.products.data.remote.dto.FakeStoreProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {

    @GET("products")
    suspend fun getProducts(): List<FakeStoreProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Long): FakeStoreProductDto

}