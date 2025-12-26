package com.example.testitrock.ui.models

import androidx.navigation3.runtime.NavKey
import com.example.testitrock.R
import com.example.testitrock.navigation.AuthNavKeys
import com.example.testitrock.navigation.ProductNavKeys

data class BottomItem(
    val routes: List<NavKey>,
    val icon: Int,
    val label: String
)

val bottomItems = listOf(
    BottomItem(listOf(ProductNavKeys.Home, ProductNavKeys.ProductDetail(1), ProductNavKeys.Payment(1)), R.drawable.home, "Home"),
    BottomItem(listOf(ProductNavKeys.PurchaseHistory), R.drawable.list, "Historial"),
    BottomItem(listOf(AuthNavKeys.Sesion), R.drawable.person, "Sesión")
)
