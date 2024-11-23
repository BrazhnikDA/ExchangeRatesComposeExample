package ru.exchangerates.domain.repository

import ru.exchangerates.data.remote.ExchangeRatesDto

interface ExchangeRateRepository {

    suspend fun getLatestRates(
        base: String? = null,
        symbols: String? = null,
    ): Result<ExchangeRatesDto>
}