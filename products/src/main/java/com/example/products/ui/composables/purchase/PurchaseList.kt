package com.example.products.ui.composables.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.products.domain.models.Purchase

@Composable
fun PurchaseList(
    modifier: Modifier = Modifier,
    purchases: List<Purchase>
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(purchases, key = { it.id }) { purchase ->
            PurchaseItem(purchase = purchase)
        }
    }
}