package com.example.products.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.products.R
import com.example.products.domain.validation.CardValidationResult
import com.example.products.ui.composables.ErrorBanner
import com.example.products.ui.composables.payment.PaymentBuySuccessDialog
import com.example.products.ui.composables.payment.ValidationCardResultDialog
import com.example.products.ui.errors.PaymentError
import com.example.products.ui.events.PaymentUIEvents
import com.example.products.ui.states.PaymentState
import com.example.products.ui.utils.CreditCardVisualTransformation
import com.example.products.ui.utils.ExpirationDateVisualTransformation

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    paymentState: PaymentState,
    paymentUIEvents: PaymentUIEvents?,
    onCardNumberChange: (String) -> Unit,
    onCardHolderChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onCVVChange: (String) -> Unit,
    cvvVisible: Boolean,
    onVisibleCVVChange: (Boolean) -> Unit,
    onResetPaymentUIEvents: () -> Unit,
    onValidateCard: () -> Unit,
    onBuy: () -> Unit,
    onGoToNextScreen: () -> Unit,
    getProductDetail: () -> Unit
) {
    var showBuySuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showValidateCardDialog by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(paymentUIEvents) {
        when (paymentUIEvents) {
            is PaymentUIEvents.OnProductBuySuccess -> {
                showBuySuccessDialog = true
            }

            is PaymentUIEvents.OnValidateCard -> {
                if (paymentState.cardValidationResult == CardValidationResult.Success) {
                    onBuy()
                } else {
                    showValidateCardDialog = true
                }
            }

            else -> Unit
        }
        onDispose {
            onResetPaymentUIEvents()
        }
    }

    LaunchedEffect(Unit) {
        getProductDetail()
    }

    if (showBuySuccessDialog) {
        PaymentBuySuccessDialog(
            onDismiss = {
                showBuySuccessDialog = false
                onGoToNextScreen()
            }
        )
    }

    if (showValidateCardDialog) {
        ValidationCardResultDialog(
            onDismiss = {
                showValidateCardDialog = false
            }
        )
    }

    when {
        paymentState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    CircularProgressIndicator()
                }
            )
        }

        paymentState.error != null -> {
            val errorMessage = when (paymentState.error) {
                PaymentError.BuyProductError -> stringResource(R.string.buy_product_error)
                PaymentError.GetProductDetailError -> stringResource(R.string.get_products_error)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    ErrorBanner(errorMessage = errorMessage)
                }
            )
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.payment_title),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        OutlinedTextField(
                            value = paymentState.cardNumber,
                            onValueChange = { onCardNumberChange(it) },
                            label = { Text(stringResource(R.string.card_number_field_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = CreditCardVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = paymentState.cardHolder,
                            onValueChange = { onCardHolderChange(it) },
                            label = { Text(stringResource(R.string.card_holder_field_label)) },
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
                                onValueChange = { onExpirationDateChange(it) },
                                label = { Text(stringResource(R.string.expiration_date_field_label)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                visualTransformation = ExpirationDateVisualTransformation()
                            )

                            OutlinedTextField(
                                value = paymentState.cvv,
                                onValueChange = { onCVVChange(it) },
                                label = { Text(stringResource(R.string.cvv_field_label)) },
                                singleLine = true,
                                visualTransformation = if (cvvVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                trailingIcon = {
                                    val image = if (cvvVisible)
                                        ImageVector.vectorResource(com.example.core.R.drawable.visibility)
                                    else ImageVector.vectorResource(com.example.core.R.drawable.visibility_off)

                                    val contentDescription = if (cvvVisible)
                                        stringResource(R.string.hide_cvv_content_description)
                                    else stringResource(R.string.show_cvv_content_description)


                                    IconButton(onClick = { onVisibleCVVChange(!cvvVisible) }) {
                                        Icon(imageVector = image, contentDescription = contentDescription)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onValidateCard,
                    enabled = paymentState.buyButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(R.string.buy_product_button), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}