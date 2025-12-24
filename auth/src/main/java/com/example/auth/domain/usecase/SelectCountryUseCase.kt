package com.example.auth.domain.usecase

import com.example.core.domain.dataSource.UserPreferences
import com.example.core.domain.models.Country
import javax.inject.Inject

class SelectCountryUseCase @Inject constructor(
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(country: Country) = userPreferences.saveCountry(country)
}