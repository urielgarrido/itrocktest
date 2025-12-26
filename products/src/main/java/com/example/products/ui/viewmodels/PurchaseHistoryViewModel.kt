package com.example.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.domain.usecases.GetPurchaseHistoryUseCase
import com.example.products.ui.errors.PurchaseHistoryError
import com.example.products.ui.states.PurchaseHistoryState
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
class PurchaseHistoryViewModel @Inject constructor(
    private val getPurchaseHistoryUseCase: GetPurchaseHistoryUseCase
) : ViewModel() {

    private val _purchaseHistoryState = MutableStateFlow(PurchaseHistoryState())
    val purchaseHistoryState = _purchaseHistoryState.asStateFlow()

    fun getPurchaseHistory(userUID: String) {
        setLoading()
        viewModelScope.launch {
            getPurchaseHistoryUseCase(userUID).onEach { purchases ->
                _purchaseHistoryState.update {
                    it.copy(
                        purchases = purchases,
                        isLoading = false
                    )
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> {
                        onPurchaseHistoryError(PurchaseHistoryError.GetPurchaseHistoryError)
                    }
                }
            }.collect()
        }
    }

    private fun setLoading() {
        _purchaseHistoryState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun onPurchaseHistoryError(purchaseHistoryError: PurchaseHistoryError) {
        _purchaseHistoryState.update {
            it.copy(
                error = purchaseHistoryError,
                isLoading = false
            )
        }
    }
}