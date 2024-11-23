package ru.exchangerates.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.exchangerates.data.remote.ExchangeRatesApi
import ru.exchangerates.data.remote.ExchangeRatesApi.Companion.PRIVATE_KEY
import ru.exchangerates.data.remote.ExchangeRatesDto
import ru.exchangerates.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeApi: ExchangeRatesApi
) : ExchangeRateRepository {

    override suspend fun getLatestRates(base: String?, symbols: String?): Result<ExchangeRatesDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = exchangeApi.getLatestRates(PRIVATE_KEY, base, symbols)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("API error ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    companion object {

    }
}