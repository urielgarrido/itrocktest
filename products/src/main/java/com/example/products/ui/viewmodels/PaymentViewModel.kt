package com.example.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.domain.models.Card
import com.example.products.domain.usecases.BuyProductUseCase
import com.example.products.domain.usecases.GetProductDetailUseCase
import com.example.products.domain.usecases.ValidateCardUseCase
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
    private val buyProductUseCase: BuyProductUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val validateCardUseCase: ValidateCardUseCase
) : ViewModel() {

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()

    private val _paymentUIEvents = MutableStateFlow<PaymentUIEvents?>(null)
    val paymentUIEvents = _paymentUIEvents.asStateFlow()

    fun updateCardNumber(cardNumber: String) {
        val cleanedCardNumber = cardNumber.filter { it.isDigit() }
        val truncatedCardNumber = cleanedCardNumber.take(16)
        _paymentState.update { it.copy(cardNumber = truncatedCardNumber) }
        checkFields()
    }

    fun updateCardHolder(cardHolder: String) {
        _paymentState.update { it.copy(cardHolder = cardHolder) }
        checkFields()
    }

    fun updateExpirationDate(expirationDate: String) {
        val cleanedDate = expirationDate.filter { it.isDigit() }
        val truncatedDate = cleanedDate.take(4)
        _paymentState.update { it.copy(expirationDate = truncatedDate) }
        checkFields()
    }

    fun updateCVV(cvv: String) {
        val truncatedCVV = cvv.take(3)
        _paymentState.update { it.copy(cvv = truncatedCVV) }
        checkFields()
    }

    fun updateCVVVisible(cvvVisible: Boolean) {
        _paymentState.update { it.copy(cvvVisible = cvvVisible) }
    }

    private fun checkFields() {
        val cardNumber = _paymentState.value.cardNumber
        val cardHolder = _paymentState.value.cardHolder
        val expirationDate = _paymentState.value.expirationDate
        val cvv = _paymentState.value.cvv

        val buyButtonEnabled = cardNumber.isNotBlank() && cardHolder.isNotBlank() && expirationDate.isNotBlank() && cvv.isNotBlank()

        _paymentState.update {
            it.copy(
                buyButtonEnabled = buyButtonEnabled
            )
        }
    }

    fun validateCard() {
        val cardNumber = _paymentState.value.cardNumber
        val cardHolder = _paymentState.value.cardHolder
        val expirationDate = _paymentState.value.expirationDate
        val cvv = _paymentState.value.cvv

        val card = Card(
            cardNumber = cardNumber,
            cardHolder = cardHolder,
            expirationDate = expirationDate,
            cvv = cvv
        )
        val cardValidationResult = validateCardUseCase(card)
        _paymentState.update {
            it.copy(
                cardValidationResult = cardValidationResult
            )
        }
        onValidateCardEvent()
    }

    fun resetPaymentUIEvents() {
        _paymentUIEvents.value = null
    }

    fun onProductBuyEvent() {
        _paymentUIEvents.value = PaymentUIEvents.OnProductBuySuccess
    }

    fun onValidateCardEvent() {
        _paymentUIEvents.value = PaymentUIEvents.OnValidateCard
    }

    fun buyProduct(userUID: String) {
        val product = _paymentState.value.productToBuy
        if (product != null) {
            setLoading()
            viewModelScope.launch {
                buyProductUseCase(product, userUID).onEach { result ->
                    if (result.isSuccess) {
                        _paymentState.update { it.copy(isLoading = false) }
                        onProductBuyEvent()
                    } else {
                        onPaymentError(PaymentError.BuyProductError)
                    }
                }.catch { throwable ->
                    when (throwable) {
                        else -> {
                            onPaymentError(PaymentError.BuyProductError)
                        }
                    }
                }.collect()
            }
        }
    }

    fun getProductDetail(id: Long) {
        setLoading()
        viewModelScope.launch {
            getProductDetailUseCase(id).onEach { product ->
                _paymentState.update { it.copy(productToBuy = product, isLoading = false) }
            }.catch { throwable ->
                when (throwable) {
                    else -> {
                        onPaymentError(PaymentError.GetProductDetailError)
                    }
                }
            }.collect()
        }
    }

    private fun setLoading() {
        _paymentState.update { it.copy(isLoading = true) }
    }

    private fun onPaymentError(paymentError: PaymentError) {
        _paymentState.update {
            it.copy(
                error = paymentError,
                isLoading = false
            )
        }
    }
}