package com.example.testitrock.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class AuthNavKeys {
    @Serializable
    data object Login : AuthNavKeys(), NavKey
    @Serializable
    data object Register : AuthNavKeys(), NavKey
    @Serializable
    data object Sesion: AuthNavKeys(), NavKey
}