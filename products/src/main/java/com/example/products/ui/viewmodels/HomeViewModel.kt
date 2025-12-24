package com.example.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.domain.usecases.GetAllProductsUseCase
import com.example.products.ui.errors.HomeError
import com.example.products.ui.events.HomeUIEvents
import com.example.products.ui.states.HomeState
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
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _homeUIEvents = MutableStateFlow<HomeUIEvents?>(null)
    val homeUIEvents = _homeUIEvents.asStateFlow()


    fun getAllProducts() {
        setLoading()
        viewModelScope.launch {
            getAllProductsUseCase().onEach { products ->
                _homeState.update {
                    it.copy(
                        products = products,
                        isLoading = false
                    )
                }
            }.catch { throwable ->
                when (throwable) {
                    else -> {
                        onHomeError(HomeError.GetProductsError)
                    }
                }
            }.collect()
        }
    }

    fun onProductClick(id: Long) {
        _homeUIEvents.value = HomeUIEvents.OnProductClicked(id)
    }

    fun resetHomeUIEvents() {
        _homeUIEvents.value = null
    }

    private fun setLoading() {
        _homeState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun onHomeError(error: HomeError) {
        _homeState.update {
            it.copy(
                error = error,
                isLoading = false
            )
        }
    }

}