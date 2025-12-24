package com.example.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.domain.models.Product
import com.example.products.domain.usecases.BuyProductUseCase
import com.example.products.ui.errors.PaymentError
import com.example.products.ui.events.PaymentUIEvents
import com.example.products.ui.states.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val buyProductUseCase: BuyProductUseCase
) : ViewModel() {

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()

    private val _paymentUIEvents = MutableStateFlow<PaymentUIEvents?>(null)
    val paymentUIEvents = _paymentUIEvents.asStateFlow()

    fun updateProductToBuy(product: Product) {
        _paymentState.update { it.copy(productToBuy = product) }
    }

    fun updateCardNumber(cardNumber: String) {
        _paymentState.update { it.copy(cardNumber = cardNumber) }
        checkFields()
    }

    fun updateCardHolder(cardHolder: String) {
        _paymentState.update { it.copy(cardHolder = cardHolder) }
        checkFields()
    }

    fun updateExpirationDate(expirationDate: String) {
        _paymentState.update { it.copy(expirationDate = expirationDate) }
        checkFields()
    }

    fun updateCVV(cvv: String) {
        _paymentState.update { it.copy(cvv = cvv) }
        checkFields()
    }

    private fun checkFields() {
        val cardNumber = _paymentState.value.cardNumber
        val cardHolder = _paymentState.value.cardHolder
        val expirationDate = _paymentState.value.expirationDate
        val cvv = _paymentState.value.cvv

        val buyButtonEnabled = cardNumber.isNotBlank() && cardHolder.isNotBlank() && expirationDate.isNotBlank() && cvv.isNotBlank()

        val error = when {
            cardNumber.isBlank() -> PaymentError.EmptyCardNumber
            cardHolder.isBlank() -> PaymentError.EmptyCardHolder
            expirationDate.isBlank() -> PaymentError.EmptyExpirationDate
            cvv.isBlank() -> PaymentError.EmptyCVV
            else -> null
        }

        _paymentState.update {
            it.copy(
                buyButtonEnabled = buyButtonEnabled,
                error = error
            )
        }
    }

    fun resetPaymentUIEvents() {
        _paymentUIEvents.value = null
    }

    fun onProductBuy() {
        _paymentUIEvents.value = PaymentUIEvents.OnProductBuy
    }

    fun buyProduct(userUID: String) {
        val product = _paymentState.value.productToBuy
        if (product != null) {
            viewModelScope.launch {
                buyProductUseCase(product, userUID).onEach { result ->
                    if (result.isSuccess) {
                        onProductBuy()
                    } else {
                        onBuyProductError(PaymentError.BuyProductError)
                    }
                }.catch { throwable ->
                    when (throwable) {
                        else -> {
                            onBuyProductError(PaymentError.BuyProductError)
                        }
                    }
                }.collect()
            }
        }
    }

    private fun onBuyProductError(paymentError: PaymentError) {
        _paymentState.update {
            it.copy(
                error = paymentError,
                isLoading = false
            )
        }
    }
}