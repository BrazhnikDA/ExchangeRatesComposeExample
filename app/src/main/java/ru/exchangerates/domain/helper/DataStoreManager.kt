package ru.exchangerates.domain.helper

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import ru.exchangerates.data.remote.dto.json.LatestRatesDto

interface DataStoreManager {
    suspend fun saveLatestRatesToDataStore(latestRates: LatestRatesDto)
    suspend fun loadLatestRatesFromDataStore(): Flow<Preferences>
}
