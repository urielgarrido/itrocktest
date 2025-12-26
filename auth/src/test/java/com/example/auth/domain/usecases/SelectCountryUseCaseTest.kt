package com.example.auth.domain.usecases

import com.example.auth.domain.usecase.SelectCountryUseCase
import com.example.core.domain.dataSource.UserPreferences
import com.example.core.domain.models.Country
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectCountryUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockUserPreferences: UserPreferences

    private lateinit var selectCountryUseCase: SelectCountryUseCase

    @Before
    fun setUp() {
        selectCountryUseCase = SelectCountryUseCase(mockUserPreferences)
    }

    @Test
    fun `when use case is invoked, it should call saveCountry on userPreferences`() = runTest {
        // Given: Un país de prueba.
        val countryToSave = Country.B

        // When: Se invoca el caso de uso con el país.
        selectCountryUseCase(countryToSave)

        // Then: Verificamos que el método 'saveCountry' de 'userPreferences' fue llamado
        // exactamente una vez con el país correcto.
        coVerify(exactly = 1) { mockUserPreferences.saveCountry(countryToSave) }
    }
}
