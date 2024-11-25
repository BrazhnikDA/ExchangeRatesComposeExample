package ru.exchangerates.domain.repository

import ru.exchangerates.data.remote.dto.xml.ValCursDto

interface HistoryPeriodRepository {

    fun findCurrency(key: String) {

    }

    suspend fun getHistoryCurrencyPeriod(
        startDate: String,
        endDate: String,
        currencyId: String,
    ): Result<ValCursDto>
}