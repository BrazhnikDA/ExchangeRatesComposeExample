package ru.exchangerates.data.repository

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.exchangerates.data.local.DataStoreManagerImpl.Companion.LATEST_RATES_KEY
import ru.exchangerates.data.remote.api.CurrencyConversationApi
import ru.exchangerates.data.remote.api.HistoryPeriodApi
import ru.exchangerates.data.remote.api.LatestRatesApi
import ru.exchangerates.data.remote.dto.json.CurrencyConversationDto
import ru.exchangerates.data.remote.dto.json.LatestRatesDto
import ru.exchangerates.data.remote.dto.xml.ValCursDto
import ru.exchangerates.domain.helper.DataStoreManager
import ru.exchangerates.domain.repository.CurrencyConversationRepository
import ru.exchangerates.domain.repository.LatestRatesRepository
import javax.inject.Inject
import ru.exchangerates.domain.repository.HistoryPeriodRepository

internal class ExchangeRepositoryImpl @Inject constructor(
    private val historyPeriodApi: HistoryPeriodApi,
    private val currencyConversationApi: CurrencyConversationApi,
    private val latestRatesApi: LatestRatesApi,
    private val dataStoreManager: DataStoreManager,
    private val moshi: Moshi,
): CurrencyConversationRepository, LatestRatesRepository, HistoryPeriodRepository {

    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var cacheData: LatestRatesDto? = null

    init {
        val latestRatesAdapter: JsonAdapter<LatestRatesDto> =
            moshi.adapter(LatestRatesDto::class.java)

        scope.launch {
            dataStoreManager.loadLatestRatesFromDataStore().collect { preferences ->
                val preference = preferences[LATEST_RATES_KEY]
                if(preference != null) {
                    cacheData = latestRatesAdapter.fromJson(preference)
                }
            }
        }
    }

    override suspend fun getLatestRates(): Result<LatestRatesDto> {
        val cache = cacheData

        return if(cache == null) {
            Log.d(LOG_TAG, "getLatestRates call API data")
            runCatching {
                val ratesDto = latestRatesApi.getLatestRates()
                dataStoreManager.saveLatestRatesToDataStore(ratesDto)
                Result.success(ratesDto)
            }.getOrElse { exception ->
                Result.failure(exception)
            }
        } else {
            Log.d(LOG_TAG, "getLatestRates call cacheData")
            Result.success(cache)
        }
    }

    override suspend fun getConvertCurrency(
        apiKey: String,
        fromCurrency: String,
        toCurrency: String,
    ): Result<CurrencyConversationDto> {
        return runCatching {
            val ratesDto = currencyConversationApi.getConvertCurrency(
                apiKey = apiKey,
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
            )
            Result.success(ratesDto)
        }.getOrElse { exception ->
            Result.failure(exception)
        }
    }

    override suspend fun getHistoryCurrencyPeriod(
        startDate: String,
        endDate: String,
        currencyId: String,
    ): Result<ValCursDto> {
       return try {
            val result = historyPeriodApi.getCurrencyHistoryPeriod(
                startDate = startDate,
                endDate = endDate,
                currencyId = currencyId,
            )
            Result.success(result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    companion object {
        private const val LOG_TAG = "ExchangeRepositoryImpl"
    }
}