package com.example.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.domain.usecases.BuyProductUseCase
import com.example.products.domain.usecases.GetProductDetailUseCase
import com.example.products.ui.errors.ProductDetailError
import com.example.products.ui.events.ProductDetailUIEvents
import com.example.products.ui.states.ProductDetailState
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
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val buyProductUseCase: BuyProductUseCase
) : ViewModel() {

    private val _productDetailState = MutableStateFlow(ProductDetailState())
    val productDetailState = _productDetailState.asStateFlow()

    private val _productDetailUIEvents = MutableStateFlow<ProductDetailUIEvents?>(null)
    val productDetailUIEvents = _productDetailUIEvents.asStateFlow()

    fun getProductDetail(id: Long) {
        setLoading()
        viewModelScope.launch {
            getProductDetailUseCase(id).onEach { product ->
                _productDetailState.update {
                    it.copy(
                        product = product,
                        isLoading = false
                    )
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> {
                        onProductDetailError(ProductDetailError.GetProductDetailError)
                    }
                }
            }.collect()
        }
    }

    fun buyProduct(userUID: String) {
        val product = _productDetailState.value.product
        if (product != null) {
            viewModelScope.launch {
                buyProductUseCase(product, userUID).onEach { result ->
                    if (result.isSuccess) {
                        onProductBuy()
                    } else {
                        onProductDetailError(ProductDetailError.BuyProductError)
                    }
                }.catch { throwable ->
                    when (throwable) {
                        else -> {
                            onProductDetailError(ProductDetailError.BuyProductError)
                        }
                    }
                }.collect()
            }
        }
    }

    fun onProductBuy() {
        val product = _productDetailState.value.product
        if (product != null) {
            _productDetailUIEvents.value = ProductDetailUIEvents.OnProductBuy(product)
        }
    }

    fun onProductWant() {
        val product = _productDetailState.value.product
        if (product != null) {
            _productDetailUIEvents.value = ProductDetailUIEvents.OnProductWant(product)
        }
    }

    fun resetProductDetailUIEvents() {
        _productDetailUIEvents.value = null
    }

    private fun setLoading() {
        _productDetailState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun onProductDetailError(productDetailError: ProductDetailError) {
        _productDetailState.update {
            it.copy(
                error = productDetailError,
                isLoading = false
            )
        }
    }
}