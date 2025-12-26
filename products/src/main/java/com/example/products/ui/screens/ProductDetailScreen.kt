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
import com.example.products.ui.composables.detail.ProductDetail
import com.example.products.ui.errors.ProductDetailError
import com.example.products.ui.events.ProductDetailUIEvents
import com.example.products.ui.states.ProductDetailState

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    productDetailState: ProductDetailState,
    productDetailUIEvents: ProductDetailUIEvents?,
    getProductDetail: () -> Unit,
    onProductWant: () -> Unit,
    onResetProductDetailUIEvents: () -> Unit,
    onGoToPayment: () -> Unit
) {
    DisposableEffect(productDetailUIEvents) {
        when(productDetailUIEvents) {
            is ProductDetailUIEvents.OnProductWant -> {
                onGoToPayment()
            }
            else -> Unit
        }
        onDispose {
            onResetProductDetailUIEvents()
        }
    }

    LaunchedEffect(Unit) {
        getProductDetail()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            when {
                productDetailState.isLoading -> {
                    CircularProgressIndicator()
                }

                productDetailState.error != null -> {
                    val errorMessage = when(productDetailState.error) {
                        ProductDetailError.GetProductDetailError -> stringResource(R.string.get_products_detail_error)
                    }
                    ErrorBanner(errorMessage = errorMessage)
                }

                productDetailState.product != null -> {
                    ProductDetail(
                        product = productDetailState.product,
                        onBuyClick = onProductWant
                    )
                }
            }
        }
    )

}