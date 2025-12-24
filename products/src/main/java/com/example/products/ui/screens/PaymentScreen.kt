package com.example.products.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.products.ui.events.PaymentUIEvents
import com.example.products.ui.states.PaymentState

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    paymentState: PaymentState,
    paymentUIEvents: PaymentUIEvents?,
    onCardNumberChange: (String) -> Unit,
    onCardHolderChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onCVVChange: (String) -> Unit,
    onResetPaymentUIEvents: () -> Unit,
    onBuyClick: () -> Unit,
    onGoToNextScreen: () -> Unit
) {
    DisposableEffect(paymentUIEvents) {
        when(paymentUIEvents) {
            is PaymentUIEvents.OnProductBuy -> {
                onGoToNextScreen()
            }
            else -> Unit
        }
        onDispose {
            onResetPaymentUIEvents()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Datos de Pago",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = paymentState.cardNumber,
                    onValueChange = { if (it.length <= 16) onCardNumberChange(it) },
                    label = { Text("Número de Tarjeta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = paymentState.cardHolder,
                    onValueChange = { onCardHolderChange(it) },
                    label = { Text("Nombre del Titular") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = paymentState.expirationDate,
                        onValueChange = {
                            if (it.length <= 5) onExpirationDateChange(it)
                        },
                        label = { Text("MM/AA") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = paymentState.cvv,
                        onValueChange = { if (it.length <= 3) onCVVChange(it) },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBuyClick,
            enabled = paymentState.buyButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Comprar", style = MaterialTheme.typography.titleMedium)
        }
    }
}