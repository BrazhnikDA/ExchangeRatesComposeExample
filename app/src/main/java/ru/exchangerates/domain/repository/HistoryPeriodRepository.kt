package ru.exchangerates.domain.repository

import ru.exchangerates.data.remote.dto.xml.ValCursDto

interface HistoryPeriodRepository {

    suspend fun getHistoryCurrencyPeriod(
        startDate: String,
        endDate: String,
        currencyId: String,
    ): Result<ValCursDto>
}