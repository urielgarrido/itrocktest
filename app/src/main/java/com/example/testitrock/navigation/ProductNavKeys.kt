package com.example.testitrock.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class ProductNavKeys {
    @Serializable
    data object Home : ProductNavKeys(), NavKey
    @Serializable
    data class ProductDetail(val productId: Long) : ProductNavKeys(), NavKey
    @Serializable
    data object PurchaseHistory : ProductNavKeys(), NavKey
    @Serializable
    data object Payment : ProductNavKeys(), NavKey
}