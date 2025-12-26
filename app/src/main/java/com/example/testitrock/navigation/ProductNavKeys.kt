package com.example.testitrock.navigation

import androidx.navigation3.runtime.NavKey
import com.example.products.domain.models.Product
import kotlinx.serialization.Serializable

sealed class ProductNavKeys {
    @Serializable
    data object Home : ProductNavKeys(), NavKey
    @Serializable
    data class ProductDetail(val productId: Long) : ProductNavKeys(), NavKey
    @Serializable
    data object PurchaseHistory : ProductNavKeys(), NavKey
    @Serializable
    data class Payment(val productId: Long) : ProductNavKeys(), NavKey
}