package com.example.core.domain.dataSource

import com.example.core.domain.models.Country
import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    suspend fun saveCountry(country: Country)
    suspend fun getSelectedCountry(): Flow<Country>
}
