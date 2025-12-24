package com.example.products.ui.composables.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.products.domain.models.Product

@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onProductClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            ProductItem(
                product = product,
                modifier = Modifier.clickable { onProductClick(product.id) }
            )
        }
    }
}