package com.example.products.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.products.R
import com.example.products.ui.composables.ErrorBanner
import com.example.products.ui.composables.home.ProductList
import com.example.products.ui.errors.HomeError
import com.example.products.ui.events.HomeUIEvents
import com.example.products.ui.states.HomeState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    homeUIEvents: HomeUIEvents?,
    getAllProducts: () -> Unit,
    onProductClick: (Long) -> Unit,
    onResetHomeUIEvents: () -> Unit,
    onGoToProductDetail: (Long) -> Unit
) {

    DisposableEffect(homeUIEvents) {
        when(homeUIEvents) {
            is HomeUIEvents.OnProductClicked -> {
                onGoToProductDetail(homeUIEvents.id)
            }
            else -> Unit
        }
        onDispose {
            onResetHomeUIEvents()
        }
    }

    LaunchedEffect(Unit) {
        getAllProducts()
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            when {
                homeState.isLoading -> {
                    CircularProgressIndicator()
                }
                homeState.error != null -> {
                    val errorMessage = when(homeState.error) {
                        HomeError.GetProductsError -> stringResource(R.string.get_products_error)
                    }
                    ErrorBanner(errorMessage = errorMessage)
                }
                else -> {
                    ProductList(
                        products = homeState.products,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    )

}