package ru.exchangerates.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import ru.exchangerates.data.remote.dto.json.LatestRatesDto
import ru.exchangerates.domain.helper.DataStoreManager
import javax.inject.Inject

internal class DataStoreManagerImpl @Inject constructor(
    private val moshi: Moshi,
    private val dataStore: DataStore<Preferences>,
) : DataStoreManager {

    override suspend fun saveLatestRatesToDataStore(latestRates: LatestRatesDto) {
        val latestRatesAdapter: JsonAdapter<LatestRatesDto> = moshi.adapter(LatestRatesDto::class.java)
        val json = latestRatesAdapter.toJson(latestRates)


        dataStore.edit { preferences ->
            preferences[LATEST_RATES_KEY] = json
        }
    }

    override suspend fun loadLatestRatesFromDataStore(): Flow<Preferences> {
        return dataStore.data
    }

    companion object {
        internal const val DATA_STORE_FILE_NAME = "CBRF_CURRENCY"

        private const val LATEST_RATES = "LATEST_RATES"
        internal val LATEST_RATES_KEY = stringPreferencesKey(LATEST_RATES)
    }
}