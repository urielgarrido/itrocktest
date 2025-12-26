package com.example.products.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.products.R
import com.example.products.ui.composables.ErrorBanner
import com.example.products.ui.composables.purchase.PurchaseList
import com.example.products.ui.errors.PurchaseHistoryError
import com.example.products.ui.states.PurchaseHistoryState

@Composable
fun PurchaseHistoryScreen(
    modifier: Modifier = Modifier,
    purchaseHistoryState: PurchaseHistoryState,
    onGetPurchaseHistory: () -> Unit
) {
    LaunchedEffect(Unit) {
        onGetPurchaseHistory()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            when {
                purchaseHistoryState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                purchaseHistoryState.error != null -> {
                    val errorMessage = when(purchaseHistoryState.error) {
                        PurchaseHistoryError.GetPurchaseHistoryError -> stringResource(R.string.get_purchase_error)
                    }
                    ErrorBanner(
                        modifier = Modifier.align(Alignment.Center),
                        errorMessage = errorMessage
                    )
                }
                purchaseHistoryState.purchases != null -> {
                    PurchaseList(
                        modifier = Modifier.align(Alignment.TopStart),
                        purchases = purchaseHistoryState.purchases
                    )
                }
            }
        }
    )

}