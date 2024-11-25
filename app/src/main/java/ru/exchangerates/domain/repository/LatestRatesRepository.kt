package ru.exchangerates.domain.repository

import ru.exchangerates.data.remote.dto.json.LatestRatesDto

interface LatestRatesRepository {

    suspend fun getLatestRates(): Result<LatestRatesDto>
}