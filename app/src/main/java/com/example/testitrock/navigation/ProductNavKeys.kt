package com.example.testitrock.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class ProductNavKeys {
    @Serializable
    data object Home : ProductNavKeys(), NavKey
    @Serializable
    data object Details : ProductNavKeys(), NavKey
    @Serializable
    data object History : ProductNavKeys(), NavKey
    @Serializable
    data object Payment : ProductNavKeys(), NavKey
}