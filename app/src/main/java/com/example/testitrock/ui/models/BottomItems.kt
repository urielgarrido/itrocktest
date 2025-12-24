package com.example.testitrock.ui.models

import androidx.navigation3.runtime.NavKey
import com.example.testitrock.R
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys

data class BottomItem(
    val route: NavKey,
    val icon: Int,
    val label: String
)

val bottomItems = listOf(
    BottomItem(ProductNavKeys.Home, R.drawable.home, "Home"),
    BottomItem(ProductNavKeys.PurchaseHistory, R.drawable.list, "Historial"),
    BottomItem(AuthNavKeys.Sesion, R.drawable.person, "Sesión")
)
