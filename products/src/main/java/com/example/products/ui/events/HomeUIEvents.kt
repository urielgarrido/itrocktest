package com.example.products.ui.events

sealed interface HomeUIEvents {
    data class OnProductClicked(val id: Long) : HomeUIEvents
}