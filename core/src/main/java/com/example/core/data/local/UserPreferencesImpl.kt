package com.example.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.data.local.utils.dataStore
import com.example.core.domain.dataSource.UserPreferences
import com.example.core.domain.models.Country
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferences {

    private val dataStore = context.dataStore

    companion object {
        private val COUNTRY_KEY = stringPreferencesKey("country_key")
    }

    override suspend fun saveCountry(country: Country) {
        dataStore.edit { preferences ->
            preferences[COUNTRY_KEY] = country.name
        }
    }

    override suspend fun getSelectedCountry(): Flow<Country> =
        dataStore.data.map { preferences ->
            val countryName = preferences[COUNTRY_KEY]
            Country.entries.firstOrNull { it.name == countryName } ?: Country.A
        }
}
